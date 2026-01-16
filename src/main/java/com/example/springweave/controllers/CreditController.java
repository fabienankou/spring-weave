@RestController
@RequestMapping("/api/credit")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @PostMapping("/apply")
    public ResponseEntity<CreditApplication> apply(@RequestParam java.util.UUID orderId, @RequestParam int months) {
        // Logique pour récupérer l'order et créer la demande
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approve(@PathVariable java.util.UUID id) {
        creditService.approveCredit(id);
        return ResponseEntity.ok("Crédit approuvé et échéancier généré.");
    }
}