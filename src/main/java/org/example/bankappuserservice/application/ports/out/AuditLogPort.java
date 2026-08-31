package org.example.bankappuserservice.application.ports.out;

public interface AuditLogPort {
    void logAccess(String identifier, String status, String ipAddress, String device);
}
