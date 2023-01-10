package com.example.controllers

import com.example.KEYHOLDER
import com.example.models.*
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.stripeOrders() {
    route("stripeOrders/{id}") {
        post("/create-payment-intent") {
            val orderJson: OrderJson = call.receive()

            var amount: Long = 0

            orderJson.basketJson.products.forEach {basketProductJson ->
                val price = transaction { Product.find { Products._id eq basketProductJson.p_id }.first().price }
                amount += (price * 100).toLong() * basketProductJson.quantity
            }

            Stripe.apiKey = KEYHOLDER.stripe_api_key

            val params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("pln")
                .build()
            val paymentIntent: PaymentIntent = PaymentIntent.create(params)

            call.respond(paymentIntent.clientSecret)

            /** this piece of code should be placed in a callback, waiting for stripe's server confirmation
             * but for the sake of this task I can assume that all the orders are successful*/
            transaction {
                val basket = Basket.find(Baskets._id eq orderJson.basketJson._id).firstOrNull()
                if (basket == null) {
                    Basket.new {
                        _id = orderJson.basketJson._id
                        u_id = "orders"
                    }
                } else {
                    basket.u_id = "orders"
                    val basketProducts = BasketProduct.find(BasketProducts.b_id eq basket._id)
                    basketProducts.forEach { it.delete() }
                }
                Order.new {
                    _id = orderJson._id
                    u_id = call.parameters["id"]!!
                    b_id = orderJson.basketJson._id
                }
                orderJson.basketJson.products.forEach {
                    BasketProduct.new {
                        this._id = it._id
                        this.p_id = it.p_id
                        this.b_id = orderJson.basketJson._id
                        this.quantity = it.quantity
                    }
                }
            }

        }

    }
}

fun Application.stripeOrdersRoutes() {
    routing {
        authenticate("auth") {
            stripeOrders()
        }
    }
}