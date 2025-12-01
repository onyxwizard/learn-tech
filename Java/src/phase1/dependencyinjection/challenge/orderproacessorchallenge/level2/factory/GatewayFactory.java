package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.factory;

import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.config.Config;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.service.*;

public class GatewayFactory {
    public static PaymentGateway create(Config config) {
        String provider = config.get("payment.provider");
        if ("paypal".equalsIgnoreCase(provider)) {
            return new PayPalGateway(
                config.get("paypal.api.key"),
                config.getDouble("paypal.fee.rate", 0.029)
            );
        } else if ("stripe".equalsIgnoreCase(provider)) {
            return new StripeGateway(
                config.get("stripe.secret.key"),
                Integer.parseInt(config.get("stripe.timeout.ms"))
            );
        } else {
            throw new IllegalArgumentException("Unknown payment.provider: " + provider);
        }
    }
}