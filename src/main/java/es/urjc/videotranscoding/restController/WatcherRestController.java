package es.urjc.videotranscoding.restController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.urjc.videotranscoding.entities.Conversion;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.service.ConversionService;
import es.urjc.videotranscoding.service.OriginalService;
import es.urjc.videotranscoding.utils.FileWatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/watcher")
@Api(tags = "Watcher Api Operations")
public class WatcherRestController {


    @Autowired
    private OriginalService originalService;
    @Autowired
    private ConversionService conversionService;


    public interface Basic extends Original.Basic, Conversion.Basic {
    }

    public interface Details
            extends Original.Basic, Original.Details, Conversion.Basic, Conversion.Details {
    }

    /**
     * With this method you can see the video transcoded or the original
     *
     * @param response for the video
     * @param request  for the video
     * @param id       of the original Video or Transcoded
     * @return
     */
    @ApiOperation(value = "Watch the Original or transcode Video")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> watchVideo(HttpServletResponse response, HttpServletRequest request,
                                        @PathVariable long id) {

        Optional<Original> video = originalService.findOneVideo(id);

        if (video == null) {
            Optional<Conversion> conversion = conversionService.findOneConversion(id);
            if (conversion == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Path p1 = Paths.get(conversion.get().getPath());
                FileWatcher.fromPath(p1).with(request).with(response).serveResource();
                return new ResponseEntity<Original>(HttpStatus.OK);
            }
        } else {
            Path p1 = Paths.get(video.get().getPath());
            FileWatcher.fromPath(p1).with(request).with(response).serveResource();
            return new ResponseEntity<Original>(HttpStatus.OK);
        }

    }

}
