package org.svrohith9.patientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.svrohith9.patientservice.model.Patient;
import patient.events.PatientEvent;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {


        PatientEvent event = PatientEvent.newBuilder().setPatientId(String.valueOf(patient.getId())).setName(patient.getFirstName() + " " + patient.getLastName()).setEmail(patient.getEmail()).setEventType("PATIENT_CREATED").build();
        try {
            System.out.println(event.toByteArray().toString());
            CompletableFuture<SendResult<String, byte[]>> patientEvent = kafkaTemplate.send("patient", event.toByteArray());
            log.info("Patient event sent: {}", patientEvent.join());
        } catch (Exception e) {
            log.error("Error sending message{}", e.getMessage());
        }
    }
}
