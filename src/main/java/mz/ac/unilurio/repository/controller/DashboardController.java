package mz.ac.unilurio.repository.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import mz.ac.unilurio.repository.model.Category;
import mz.ac.unilurio.repository.model.Document;
import mz.ac.unilurio.repository.model.Type;
import mz.ac.unilurio.repository.repository.CategoryRepository;
import mz.ac.unilurio.repository.repository.DocumentRepository;
import mz.ac.unilurio.repository.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Controller
public class DashboardController {

    @Autowired
    private DocumentRepository documentRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TypeRepository typeRepository;

    private static HttpTransport HTTP_Transport = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private List<String> SCOPE = Collections.singletonList(DriveScopes.DRIVE);

    private static final String USER_INDENTIFIER_KEY = "MY_DUMMY_USER";

    @Value("${google.oauth.callback.uri}")
    private String CALLBACK_URI;

    @Value("${google.secret.key.path}")
    private Resource gdSecretKey;


    @Value("${google.credentials.folder.path}")
    private Resource credentialsFolder;

    private GoogleAuthorizationCodeFlow flow;

    @PostConstruct
    public void init() throws IOException {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(gdSecretKey.getInputStream()));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_Transport, JSON_FACTORY, secrets, SCOPE)
                .setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile())).build();

    }

    @GetMapping(value = "/admin")
    public String showAdmin() throws Exception {
        boolean isAutenticated = false;

        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);
        if (credential != null) {
            isAutenticated = credential.refreshToken();
        }
        return isAutenticated ? "home.html" : "index.html";
    }


    @GetMapping(value = {"/googlesignin"})
    public void doGoogleSignIn(HttpServletResponse response) throws IOException {

        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectURL = url.setRedirectUri(CALLBACK_URI).setAccessType("offline").build();
        response.sendRedirect(redirectURL);
    }

    @GetMapping(value = "/oauth")
    public String saveAuthorazationCode(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        if (code != null) {
            saveCode(code);
            return "dashboard.html";
        }
        return "home.html";

//        return "dashboard.html";
    }

    private void saveCode(String code) throws Exception {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(CALLBACK_URI).execute();
        flow.createAndStoreCredential(response, USER_INDENTIFIER_KEY);
    }

    @GetMapping(value = "/create")
    private void create(HttpServletResponse response) throws IOException {

        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);


        if (credential != null) {

            Drive drive = new Drive.Builder(HTTP_Transport, JSON_FACTORY, credential).setApplicationName("unilurio-documentRepository").build();

            File file = new File();

            file.setTitle("image.jpg");
            FileContent content = new FileContent("Image/jpg", new java.io.File("C:\\Users\\user\\IdeaProjects\\documentRepository\\src\\main\\resources\\file\\image.jpg"));
            File uploadedFile = drive.files().insert(file, content).setFields("id").execute();
            String fileRef = String.format("{fileID: '%s'}", uploadedFile.getId());

            response.getWriter().write(fileRef);
        }
    }


    @GetMapping(value = "/uploadinfolder")
    private void uploadInFolder(HttpServletResponse response) throws IOException {

        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);
        Drive drive = new Drive.Builder(HTTP_Transport, JSON_FACTORY, credential).setApplicationName("unilurio-documentRepository").build();

        File file = new File();
        file.setTitle("image.jpg");
        file.setParents(Arrays.asList(new ParentReference().setId("1nh1bGiu5JLYWjCvUofyuc1vZnuZvsf_r")));
        FileContent content = new FileContent("Image/jpg", new java.io.File("C:\\Users\\user\\IdeaProjects\\documentRepository\\src\\main\\resources\\file\\image.jpg"));
        File uploadedFile = drive.files().insert(file, content).setFields("id").execute();
        String fileRef = String.format("{fileID: '%s'}", uploadedFile.getId());

        response.getWriter().write(fileRef);

    }


    @PostMapping(value = "/addallfiles", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Object> addAllFiles() throws IOException {

        FileList fileList = findGDriveFiles("1nh1bGiu5JLYWjCvUofyuc1vZnuZvsf_r");
        Map<String, Document> map = convertDBFilesToHashMap(documentRepository.findAll());

        int totalAddedFiles = 0;

        for (File file : fileList.getItems()) {

            if (!map.containsKey(file.getId())) {

                Document document = new Document();

                document.setTitle(file.getTitle());
                document.setGoogleId(file.getId());
                document.setUrl("https://drive.google.com/open?id="+file.getId());

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(Calendar.getInstance().getTime());
                document.setYear(calendar.get(Calendar.YEAR));
                document.setDatecreate(calendar.getTime());

                document.setCategory(categoryRepository.findById(6).get());
                document.setType(typeRepository.findById(1).get());

                documentRepository.save(document);
                totalAddedFiles++;
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", true);
        result.put("total", totalAddedFiles);

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }


    private FileList findGDriveFiles(String parentID) throws IOException {

        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);
        Drive drive = new Drive.Builder(HTTP_Transport, JSON_FACTORY, credential).setApplicationName("unilurio-documentRepository").build();

        List<FileDisplay> responseList = new ArrayList<>();
        FileList fileList = drive.files().list().setQ("'1nh1bGiu5JLYWjCvUofyuc1vZnuZvsf_r' in parents").setFields("items(id,title)").execute();

        return fileList;
    }

    private Map<String, Document> convertDBFilesToHashMap(Iterable<Document> documents) {

        Map<String, Document> map = new HashMap<String, Document>();
        documents.forEach(document -> {
            map.put(document.getGoogleId(), document);
        });

        return map;

    }

    @GetMapping(value = "/listfiles", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Object> listFiles() throws IOException {

        FileList fileList = findGDriveFiles("1nh1bGiu5JLYWjCvUofyuc1vZnuZvsf_r");
        Map<String, Document> map = convertDBFilesToHashMap(documentRepository.findByFilter(null, null, null, null));

        List<FileDisplay> responseList = new ArrayList<>();
        for (File file : fileList.getItems()) {
            if (!map.containsKey(file.getId())) {
                FileDisplay itemDTO = new FileDisplay();

                itemDTO.setId(file.getId());
                itemDTO.setTitle(file.getTitle());
                itemDTO.setType(file.getDescription());
                String html = "<a href=\"#\" rel=\"" + file.getId() + "\" class=\"add btn btn-default\" title=\"Adicionar\" data-toggle=\"tooltip\">Adicionar</a>";
                itemDTO.setAction(html);

                responseList.add(itemDTO);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", responseList);
        result.put("recordsTotal", responseList.size());
        result.put("draw", 1);
        result.put("recordsFiltered", responseList.size());

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }


    @GetMapping(value = "/document/{id}", produces = "application/json")
    public @ResponseBody
    Document document(@PathVariable(name = "id") String id) throws IOException {
        Document document = documentRepository.findByGoogleId(id);
        return document;
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Object> delete(@PathVariable(name = "id") String id) throws IOException {
        Document document = documentRepository.findByGoogleId(id);
        documentRepository.delete(document);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", true);

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }


    @PostMapping(value = "/edit", produces = "application/json")
    public @ResponseBody
    ResponseEntity<Object> edit(@Valid @ModelAttribute("document") Document document) throws IOException {
        Document updateDoc = documentRepository.findById(document.getId()).get();
        document.setDocument(updateDoc.getDocument());
        documentRepository.save(document);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", true);

        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/category/all", produces = "application/json")
    public @ResponseBody
    List<Category> categories() throws IOException {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @GetMapping(value = "/type/all", produces = "application/json")
    public @ResponseBody
    List<Type> typeList() throws IOException {
        List<Type> types = typeRepository.findAll();
        return types;
    }


    @DeleteMapping(value = "/deletefile/{fileId}", produces = "application/json")
    public @ResponseBody
    Message deleteFile(@PathVariable(name = "fileId") String fileId) throws IOException {
        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);

        Drive drive = new Drive.Builder(HTTP_Transport, JSON_FACTORY, credential).setApplicationName("unilurio-documentRepository").build();

        drive.files().delete(fileId).execute();

        Message message = new Message();
        message.setMessage("File has been deleted.");

        return message;
    }

    @PostMapping(value = "/makepublic/{fileId}", produces = "application/json")
    public @ResponseBody
    Message makePublic(@PathVariable(name = "fileId") String fileId) throws IOException {
        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);

        Drive drive = new Drive.Builder(HTTP_Transport, JSON_FACTORY, credential).setApplicationName("unilurio-documentRepository").build();

        Permission permission = new Permission();

        permission.setType("anyone");
        permission.setRole("reader");


        drive.permissions().insert(fileId, permission).execute();

        Message message = new Message();
        message.setMessage("Permission has been successfuly granted.");

        return message;
    }

    @GetMapping(value = "/createfolder/{foldername}")
    public @ResponseBody
    Message createFolder(@PathVariable(name = "foldername") String foldername) throws IOException {
        Credential credential = flow.loadCredential(USER_INDENTIFIER_KEY);

        Drive drive = new Drive.Builder(HTTP_Transport, JSON_FACTORY, credential).setApplicationName("unilurio-documentRepository").build();

        File file = new File();

        file.setTitle(foldername);
        file.setMimeType("application/vnd.google-apps.folder");
        drive.files().insert(file).execute();

        Message message = new Message();
        message.setMessage("File has been deleted.");

        return message;
    }

    class Message {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
