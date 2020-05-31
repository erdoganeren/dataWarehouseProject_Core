package pacApp.pacController;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pacApp.pacData.UserRepository;
//import pacApp.pacException.RegistrationBadRequestException;
import pacApp.pacException.RegistrationBadRequestException;
import pacApp.pacModel.User;
import pacApp.pacModel.pacResponse.GenericResponse;
import pacApp.pacSecurity.JwtAuthenticatedProfile;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController extends BaseRestController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repository;

  
    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    @CrossOrigin
    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.repository.findOneByEmail(userEmail);

        if (!optUser.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();
        long userId = user.getId();
        String email = user.getEmail();

        if (userId != 1L && !email.equals("admin@carrental.com")) {
            GenericResponse response = new GenericResponse(HttpStatus.FORBIDDEN.value(),"Request forbidden");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN);
        }

        List<User> users = this.repository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserInfo() {
        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.repository.findOneByEmail(userEmail);

        if (!optUser.isPresent()){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> registerUser(@RequestBody User user){
        log.info("registerUser: " + user.toString());

        if (user.getEmail() == null || user.getPassword() == null) {
            throw new RegistrationBadRequestException();
        }

        EmailValidator emailValidator = EmailValidator.getInstance();

        if (!emailValidator.isValid(user.getEmail())) {
            throw new RegistrationBadRequestException();
        }

        Optional<User> optUser = this.repository.findOneByEmail(user.getEmail());

        if (optUser.isPresent()){
            GenericResponse response = new GenericResponse(HttpStatus.CONFLICT.value(),"User already registered");
            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }

        RegexValidator validator = new RegexValidator("((?=.*[a-z])(?=.*\\d)(?=.*[@#$%])(?=.*[A-Z]).{6,16})");

        if (!validator.isValid(user.getPassword())) {
            throw new RegistrationBadRequestException();
        }

        user.setId(0L);

        this.repository.saveUser(user);

        GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "User registration successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestBody User userRequest) {
        if (userRequest == null) {
            GenericResponse response = new GenericResponse(400, "Missing request body");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.repository.findOneByEmail(userEmail);

        if (!optUser.isPresent()){
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();
        User userCopy = null;

        try {
            userCopy = (User) user.clone();
        } catch (CloneNotSupportedException ex) {
            log.error(ex.getMessage());
        }
        //save user settings

        user = this.repository.saveAndFlush(user);

        //validate changes

        if (userCopy == null) {
            GenericResponse response = new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"User update failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userCopy.equals(user)) {
            GenericResponse response = new GenericResponse(HttpStatus.OK.value(),"User settings not changed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        GenericResponse response = new GenericResponse(HttpStatus.OK.value(),"User settings updated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
