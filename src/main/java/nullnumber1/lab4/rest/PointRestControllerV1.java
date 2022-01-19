package nullnumber1.lab4.rest;

import lombok.extern.slf4j.Slf4j;
import nullnumber1.lab4.model.BasicPoint;
import nullnumber1.lab4.model.Point;
import nullnumber1.lab4.security.jwt.JwtTokenProvider;
import nullnumber1.lab4.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/model/")
public class PointRestControllerV1 {
    private final JwtTokenProvider jwtTokenProvider;
    private final PointService pointService;

    @Autowired
    public PointRestControllerV1(JwtTokenProvider jwtTokenProvider, PointService pointService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.pointService = pointService;
    }

    @RequestMapping(value = "points", method = RequestMethod.GET)
    public ResponseEntity<?> getPoints(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null) return ResponseEntity.badRequest().body("Token is invalid");

        Long id = jwtTokenProvider.getId(token);
        List<Point> pointList = pointService.getAllByUser(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("in getPoints() – list of points was returned:\n{}", pointList);
        return new ResponseEntity<>(pointList, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "points/graph", params = {"r"}, method = RequestMethod.GET)
    public ResponseEntity<?> recalculatedRadiusPoints(HttpServletRequest request, @RequestParam(value = "r") double r) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) return ResponseEntity.badRequest().body("Token is invalid");

        Long id = jwtTokenProvider.getId(token);
        List<Point> pointList = pointService.getAllByUser(id);
        List<BasicPoint> basicPoints = new ArrayList<>();

        for (Point p : pointList) {
            BasicPoint basicPoint = new BasicPoint();
            basicPoint.setX(p.getX());
            basicPoint.setY(p.getY());
            basicPoint.setR(r);
            basicPoint.validate();
            basicPoints.add(basicPoint);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("in recalculatedRadiusPoints() – list of basic points was returned:\n{}", basicPoints);
        return new ResponseEntity<>(basicPoints, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "points", method = RequestMethod.POST)
    public ResponseEntity<?> addPoint(HttpServletRequest request, @RequestBody Point point) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null) return ResponseEntity.badRequest().body("Token is invalid");

        Long id = jwtTokenProvider.getId(token);
        point.setInitiatorId(id);
        point.validate();

        if (!pointService.save(point)) {
            log.info("in addPoint() – point was not added:\n{}", point);
            return new ResponseEntity<>(point, HttpStatus.BAD_REQUEST);
        }

        log.info("in addPoint() – point was added:\n{}", point);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }
}
