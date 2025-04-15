package servlet;

import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.nio.file.Paths;

import static spark.Spark.*;

public class ServerPayment {
    public static void main(String[] args) {
        port(3000);
        String clientId = "10309db4-d116-4c91-9ba8-1a95a495e6dd";
        String apiKey = "f0e1b0e7-c815-4f93-b3e0-4840e9e45353";
        String checksumKey = "9246f470e539cb0d64a4cc128007653352c28d84e5603748c70a03ffce9f1b35";

        final PayOS payOS = new PayOS(clientId, apiKey, checksumKey);

        staticFiles.externalLocation(
                Paths.get("public").toAbsolutePath().toString());

        post("/create-payment-link", (request, response) -> {
            String domain = "http://localhost:8080/Hankyo";
            Long orderCode = System.currentTimeMillis() / 1000;
            ItemData itemData = ItemData
                    .builder()
                    .name("")
                    .quantity(1)
                    .price(2000)
                    .build();

            PaymentData paymentData = PaymentData
                    .builder()
                    .orderCode(orderCode)
                    .amount(2000)
                    .description("Thanh toán đơn hàng")
                    .returnUrl(domain)
                    .cancelUrl(domain)
                    .item(itemData)
                    .build();

            CheckoutResponseData result = payOS.createPaymentLink(paymentData);
            response.redirect(result.getCheckoutUrl(), 303);
            return "";
        });
    }
}