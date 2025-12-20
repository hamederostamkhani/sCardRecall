package ir.hamedrostamkhani.backend.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return Optional.empty();
//        }
//
//        Object principal = authentication.getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            return Optional.of(((UserDetails) principal).getUsername());
//        } else {
//            return Optional.of(principal.toString());
//        }

        return Optional.of("system");
    }
}
