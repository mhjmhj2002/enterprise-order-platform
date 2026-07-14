package com.mercadoaurora.inventory.application.port.in;

import com.mercadoaurora.inventory.application.command.RecognizeOrderConfirmationCommand;

public interface OrderConfirmationRecognizer {
    boolean execute(RecognizeOrderConfirmationCommand command);
}
