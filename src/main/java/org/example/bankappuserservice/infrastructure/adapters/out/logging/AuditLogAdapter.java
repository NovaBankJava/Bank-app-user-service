package org.example.bankappuserservice.infrastructure.adapters.out.logging;


import org.example.bankappuserservice.application.ports.out.AuditLogPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditLogAdapter implements AuditLogPort {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogAdapter.class);

    @Override
    public void logAccess(String identifier, String status, String ipAddress, String device) {
        logger.info("[AUDIT LOG] Data/Hora: {} | Usuário: {} | Status: {} | IP: {} | Dispositivo: {}",
                LocalDateTime.now(), identifier, status, ipAddress, device);
    }
}
