package org.svrohith9.patientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.svrohith9.patientservice.model.Patient;
import patient.events.PatientEvent;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    
    @Value("${spring.kafka.topic.patient-events:patient-events}")
    private String patientTopic;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient, String eventType) {
        log.info("Sending {} event for patient: {}", eventType, patient.getId());
        
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId() != null ? patient.getId().toString() : "")
                .setName(patient.getFirstName() + " " + patient.getLastName())
                .setEmail(patient.getEmail())
                .setEventType(eventType)
                .build();

        try {
            CompletableFuture<SendResult<String, byte[]>> sendResult = 
                    kafkaTemplate.send(patientTopic, patient.getId().toString(), event.toByteArray());
            
            sendResult.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully sent {} event for patient {} to partition {}", 
                            eventType, patient.getId(), result.getRecordMetadata().partition());
                } else {
                    log.error("Failed to send {} event for patient {}. Error: {}", 
                            eventType, patient.getId(), ex.getMessage());
                }
            });
            
            log.debug("Patient event sent: {} for patient: {}", eventType, patient.getId());
        } catch (Exception e) {
            log.error("Error sending patient event: {}", e.getMessage(), e);
            throw e;
        }
    }
}
