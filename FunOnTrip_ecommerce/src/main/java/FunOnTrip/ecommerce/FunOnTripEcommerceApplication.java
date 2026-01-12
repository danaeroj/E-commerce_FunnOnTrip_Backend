package FunOnTrip.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
  basePackages = "FunOnTrip.ecommerce",
  excludeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "FunOnTrip\\.ecommerce\\.controller\\.Contacto.*"),
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "FunOnTrip\\.ecommerce\\.service\\.Contacto.*"),
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "FunOnTrip\\.ecommerce\\.repository\\.Contacto.*"),
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "FunOnTrip\\.ecommerce\\.model\\.Contacto.*")
  }
)
public class FunOnTripEcommerceApplication { 

    public static void main(String[] args) {
        SpringApplication.run(FunOnTripEcommerceApplication.class, args);
    }
}
