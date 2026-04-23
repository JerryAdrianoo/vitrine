package com.vitrine.web.mapper;

import com.vitrine.api.dto.PaymentResponse;
import com.vitrine.api.model.Payment;

public class PaymentMapperUtil {

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getProcessedAt()
        );
    }

    private PaymentMapperUtil() {}
}
