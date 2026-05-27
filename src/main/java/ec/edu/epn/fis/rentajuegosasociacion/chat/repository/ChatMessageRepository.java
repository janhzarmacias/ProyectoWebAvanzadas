package ec.edu.epn.fis.rentajuegosasociacion.chat.repository;

import ec.edu.epn.fis.rentajuegosasociacion.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    // Trae todo el historial de una categoría, del más antiguo al más reciente
    List<ChatMessage> findByCategoryIdOrderByTimestampAsc(Long categoryId);
}
