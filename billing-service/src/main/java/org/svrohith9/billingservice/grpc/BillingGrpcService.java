package org.svrohith9.billingservice.grpc;

import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(BillingGrpcService.class);

    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> responseObserver) {
        logger.info("Billing service createBillingAccount method invoked request: {}", billingRequest);
        billing.BillingResponse billingResponse = billing.BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("Active")
                .build();

        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}
