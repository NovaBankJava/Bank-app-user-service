package org.example.bankappuserservice.userRegistration.infra.adapter.outbound;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class IdGeneratorAdapterTest {

    private final IdGeneratorAdapter idGeneratorAdapter = new IdGeneratorAdapter();

    @Test
    void shouldGenerateRandomId() {

      String  randomId = idGeneratorAdapter.generateId();

      assertNotNull(randomId);
      assertFalse(randomId.isBlank());
      assertDoesNotThrow(() -> UUID.fromString(randomId));
    }
}
