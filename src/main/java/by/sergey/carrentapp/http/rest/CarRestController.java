package by.sergey.carrentapp.http.rest;

import by.sergey.carrentapp.service.CarService;
import by.sergey.carrentapp.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarRestController {

    private final CarService carService;

    /*@GetMapping(value = "/{id}/car_image", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findImage(@PathVariable("id") Long id) {
        return carService.findCarImage(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }*/

    @GetMapping(value = "/{id}/car_image")
    public ResponseEntity<byte[]> findImage(@PathVariable("id") Long id) {
        return carService.findCarImage(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
