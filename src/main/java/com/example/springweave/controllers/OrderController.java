import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Order> placeOrder(@RequestBody CheckoutRequest request, @AuthenticationPrincipal Customer customer) {
        Order order = orderService.createOrder(customer, request.items(), request.shippingAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}