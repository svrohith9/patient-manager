package org.svrohith9.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
@Slf4j
public class BillingServiceGrpcClient {

    private final String billingServiceHost;
    private final int billingServicePort;
    private ManagedChannel channel;
    private BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${grpc.billing.service.host:localhost}") String billingServiceHost,
            @Value("${grpc.billing.service.port:9001}") int billingServicePort) {
        this.billingServiceHost = billingServiceHost;
        this.billingServicePort = billingServicePort;
    }

    @PostConstruct
    public void init() {
        log.info("Initializing gRPC channel to billing service at {}:{}", billingServiceHost, billingServicePort);
        this.channel = ManagedChannelBuilder
                .forAddress(billingServiceHost, billingServicePort)
                .usePlaintext()
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
        this.blockingStub = BillingServiceGrpc.newBlockingStub(channel);
        log.info("gRPC channel initialized successfully");
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            log.info("Shutting down gRPC channel");
            try {
                channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn("Interrupted while shutting down gRPC channel", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        log.info("Creating billing account for patient: {} ({})", name, patientId);

        try {
            BillingRequest request = BillingRequest.newBuilder()
                    .setPatientId(patientId)
                    .setName(name)
                    .setEmail(email)
                    .build();

            BillingResponse response = blockingStub.createBillingAccount(request);
            log.info("Billing account created successfully. Account ID: {}", response.getAccountId());
            return response;

        } catch (StatusRuntimeException e) {
            log.error("gRPC error while creating billing account: {} - {}", 
                    e.getStatus().getCode(), e.getStatus().getDescription());
            throw new RuntimeException("Failed to create billing account: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error creating billing account: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create billing account: " + e.getMessage(), e);
        }
    }
}
