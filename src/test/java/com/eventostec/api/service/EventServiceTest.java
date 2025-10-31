package com.eventostec.api.service;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDto;
import com.eventostec.api.domain.event.EventRequestDto;
import com.eventostec.api.repositories.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// @ExtendWith do JUnit 5: ativa extensões (aqui habilita o suporte do Mockito para @Mock/@InjectMocks).
public class EventServiceTest {

    @Mock
    // @Mock (Mockito) cria um mock para isolar dependências e controlar comportamento em testes.
    private EventRepository eventRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private CouponService couponService;

    @Mock
    private com.amazonaws.services.s3.AmazonS3 s3Client;

    @InjectMocks
    // @InjectMocks (Mockito) injeta os mocks acima na instância da classe que estamos testando.
    private EventService eventService;

    @Test
    // @Test (JUnit) marca o método como caso de teste a ser executado pelo framework de testes.
    public void createEvent_whenNotRemote_shouldSaveEventAndCreateAddress() {
        // Arrange
        EventRequestDto dto = new EventRequestDto(
                "Título",
                "Descrição",
                System.currentTimeMillis(),
                "Cidade",
                "UF",
                false, // remote = false
                "http://example.com",
                null // sem imagem para simplificar
        );

        // Mockito: faz com que o repositório retorne o mesmo evento que for salvo.
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Event created = eventService.createEvent(dto);

        // Assert
        Assertions.assertEquals("Título", created.getTitle());
        Assertions.assertFalse(created.getRemote());

        // Mockito: verifica que o addressService.createAddress foi chamado exatamente uma vez.
        verify(addressService, times(1)).createAddress(eq(dto), any(Event.class));
    }

    @Test
    // @Test (JUnit) marca o método como caso de teste a ser executado pelo framework de testes.
    public void getEventsDetails_shouldReturnDtoWithCoupons() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Evento X");
        event.setDescription("Desc");
        event.setDate(new Date());
        Address address = new Address();
        address.setCity("Cidade");
        address.setUf("UF");
        address.setEvent(event);
        event.setAddress(address);

        // Mockito: definir comportamentos dos mocks
        when(eventRepository.findById(eq(eventId))).thenReturn(Optional.of(event));

        Coupon c = new Coupon();
        c.setCode("ABC123");
        c.setDiscount(10);
        c.setValid(new Date());
        List<Coupon> coupons = List.of(c);

        // Mockito: aqui usamos any(Date.class) porque a data passada no método é gerada internamente (new Date()).
        when(couponService.consultCoupons(eq(eventId), any(Date.class))).thenReturn(coupons);

        // Act
        EventDetailsDto dto = eventService.getEventsDetails(eventId);

        // Assert
        Assertions.assertEquals(eventId, dto.id());
        Assertions.assertEquals("Evento X", dto.title());
        Assertions.assertEquals(1, dto.coupons().size());

        // Mockito: garante que o serviço de cupom foi consultado.
        verify(couponService, times(1)).consultCoupons(eq(eventId), any(Date.class));
    }
}

