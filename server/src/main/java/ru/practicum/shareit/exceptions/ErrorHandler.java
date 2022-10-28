package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {


    @ExceptionHandler({EmailAlreadyExistException.class})
    public ResponseEntity<String> entityAlreadyExist(EmailAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EntityNotFoundException.class, BookingCreationException.class})
    public ResponseEntity<String> entityNotFoundException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnknownBookingStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse unknownStateBooking(UnknownBookingStateException e) {
        log.error(e.getMessage());
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> validateException(MethodArgumentNotValidException e) {
        log.error(e.getParameter().toString());
        return new ResponseEntity<>(e.getParameter().getExecutable().toGenericString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ItemAvailableException.class, CommentCreationException.class,
            BookingStatusException.class, PaginationParametersException.class})
    public ResponseEntity<String> itemAvailableException(RuntimeException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<String> unknownException(Throwable e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
