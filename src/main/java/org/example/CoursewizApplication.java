package org.example;


import io.dropwizard.Application;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.example.application.api.users.UserAuthenticated;
import org.example.application.dao.coursera.*;
import org.example.application.dao.users.UserInstructorDAO;
import org.example.application.dao.users.UserStudentDAO;
import org.example.application.exceptions.maps.*;
import org.example.application.repository.coursera.*;
import org.example.application.repository.users.UserInstructorRepository;
import org.example.application.repository.users.UserStudentRepository;
import org.example.application.resource.coursera.*;
import org.example.application.resource.users.UserInstructorResource;
import org.example.application.resource.users.UserStudentResource;
import org.example.application.services.implementations.coursera.*;
import org.example.application.services.implementations.users.UserInstructorServiceImpl;
import org.example.application.services.implementations.users.UserStudentServiceImpl;
import org.example.application.services.interfaces.coursera.*;
import org.example.application.services.interfaces.users.UserInstructorService;
import org.example.application.services.interfaces.users.UserStudentService;
import org.example.application.services.jwt.JwtAuthFilter;
import org.example.db.JdbcHelper;
import org.example.db.JdbcHelperImpl;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.ws.rs.ext.ExceptionMapper;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class CoursewizApplication extends Application<CoursewizConfiguration> {

    private static final Logger log= LoggerFactory.getLogger(CoursewizApplication.class);

    public static void main(String[] args) throws Exception {
        new CoursewizApplication().run("server", "config.yml");
    }

    @Override
    public void initialize(Bootstrap<CoursewizConfiguration> bootstrap) {

    }

    @Override
    public void run(CoursewizConfiguration coursewizConfiguration, Environment environment) throws Exception {

        final DataSourceFactory dataSourceFactory = coursewizConfiguration.getDataSourceFactory();

        // Create and manage the DataSource for you
        final ManagedDataSource dataSource = dataSourceFactory.build(environment.metrics(), "postgresql");
        // For JDBC:
        // You can pass dataSource directly to resources that need it

        // For JDBI (optional):
        // final Jdbi jdbi = new JdbiFactory().build(environment, dataSourceFactory, "postgresql");

        // Register your resource and inject the DataSource


        JerseyEnvironment jersey = environment.jersey();
        registerServices(dataSource,jersey);
        mapExceptions(jersey);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword())
                .schemas("public")
                .locations("classpath:db/migration")
                .load();

        // Run migrations
        flyway.migrate();
    }

    private static void mapExceptions(JerseyEnvironment jersey) {
        ExceptionMapper exceptions[]={
                new DuplicateEntityExceptionMapper(),
                new NotFoundExceptionMapper(),
                new InvalidUserCredentialsMapper(),
                new EntityInserFailedExceptionMapper(),
                new EntityUpdateFailedExceptionMapper(),
                new UserTokenExceptionMapper(),
                new UserTokenInvalidDateExceptionMapper(),
                new UserLoginExceptionMapper(),
                new StudentCourseReportExceptionMapper()
        };
        for (ExceptionMapper exception:exceptions){
            jersey.register(exception);
        }
    }

    private void registerServices(DataSource dataSource, JerseyEnvironment jersey) {
        
        JdbcHelper jdbcHelper = new JdbcHelperImpl(dataSource);

        //repos
        //coursera
        InstructorRepository instructorRepository = new InstructorDAO(jdbcHelper);
        CourseRepository courseRepository = new CourseDAO(jdbcHelper);
        StudentRepository studentRepository = new StudentDAO(jdbcHelper);
        StudentCourseRepository studentCourseRepository = new StudentCourseDAO(jdbcHelper);
        StudentCourseReportRepository studentCourseReportRepository =new StudentCourseReportDAO(jdbcHelper);
        //users
        UserStudentRepository userStudentRepository=new UserStudentDAO(jdbcHelper);
        UserInstructorRepository userInstructorRepository = new UserInstructorDAO(jdbcHelper);

        //services
        //jwt
//        jersey.register(new JwtAuthFilter());
        jersey.register(RolesAllowedDynamicFeature.class);
        jersey.register(new JwtAuthFilter());
        jersey.register(new AuthValueFactoryProvider.Binder<UserAuthenticated>(UserAuthenticated.class));

        //coursera
        CourseService courseService = new CourseServiceImpl(courseRepository, instructorRepository);
        InstructorService instructorService = new InstructorServiceImpl(instructorRepository);
        StudentService studentService = new StudentServiceImpl(studentRepository);
        StudentCourseService studentCourseService = new StudentCourseServiceImpl( studentCourseRepository);
        StudentCourseReportService studentCourseReportService=new StudentCourseReportServiceImpl(studentCourseReportRepository, studentRepository);

        //users
        UserStudentService userStudentService=new UserStudentServiceImpl(userStudentRepository);
        UserInstructorService userInstructorService = new UserInstructorServiceImpl(userInstructorRepository);

        //resources
        //coursera
        StudentResource studentResource = new StudentResource(studentService);
        InstructorResource instructorResource = new InstructorResource(instructorService);
        CourseResource courseResource = new CourseResource(courseService);
        StudentCourseResource studentCourseResource = new StudentCourseResource(studentCourseService);
        StudentCourseReportResource studentCourseReportResource = new StudentCourseReportResource(studentCourseReportService);

        //users
        UserStudentResource userStudentResource=new UserStudentResource(userStudentService);
        UserInstructorResource userInstructorResource = new UserInstructorResource(userInstructorService);


        //registers
        jersey.register(studentResource);
        jersey.register(instructorResource);
        jersey.register(courseResource);
        jersey.register(studentCourseResource);
        jersey.register(studentCourseReportResource);
        jersey.register(userStudentResource);
        jersey.register(userInstructorResource);
    }

//     TEST: test getting permission set

//    public static Set<String> getPermission(ManagedDataSource dataSource){
//        String sql = "SELECT ARRAY_AGG(p.name) AS permissions\n" +
//                "FROM permissions p\n" +
//                "JOIN role_permissions rp ON p.id = rp.permission_id\n" +
//                "WHERE rp.role_id = ?;\n";
//        try(Connection conn=dataSource.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, 6);
//
//            ResultSet rs = stmt.executeQuery();
//            Set<String> permissions = new HashSet<>();
//
//            if (rs.next()) {
//
//
//            }
//            return permissions;
//        } catch (Exception e) {
//            return null;
//        }
//
//    }



}