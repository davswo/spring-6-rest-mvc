package dsw.springframework.spring6restmvc.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
