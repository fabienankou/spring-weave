@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener // S'exécute seulement si la transaction DB est réussie
    public void handleOrderCreated(OrderCreatedEvent event) {
        Order order = event.getOrder();
        notificationService.send(
                order.getCustomer().getId(),
                order.getCustomer().getEmail(),
                "Commande confirmée !",
                "Votre commande " + order.getOrderNumber() + " a été reçue.",
                NotificationChannel.EMAIL
        );
    }
}