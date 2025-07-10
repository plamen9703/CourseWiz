package org.example;


import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.example.application.dao.*;
import org.example.application.exceptions.maps.*;
import org.example.application.jwt.JwtAuthFilter;
import org.example.application.jwt.JwtService;
import org.example.application.repository.*;
import org.example.application.resource.*;
import org.example.application.services.*;
import org.example.application.services.interfaces.*;
import org.example.db.JdbcHelper;
import org.example.db.JdbcHelperImpl;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import javax.ws.rs.ext.ExceptionMapper;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class CoursewizApplication extends Application<CoursewizConfiguration> {

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
                new EntityUpdateFailedExceptionMapper()
        };
        for (ExceptionMapper exception:exceptions){
            jersey.register(exception);
        }
    }

    private void registerServices(DataSource dataSource, JerseyEnvironment jersey) {
        
        JdbcHelper jdbcHelper = new JdbcHelperImpl(dataSource);

        //repos
        InstructorRepository instructorRepository = new InstructorDAO(jdbcHelper);
        CourseRepository courseRepository = new CourseDAO(jdbcHelper);
        StudentRepository studentRepository = new StudentDAO(jdbcHelper);
        StudentCourseRepository studentCourseRepository = new StudentCourseDAO(jdbcHelper);
        StudentCourseReportRepository studentCourseReportRepository =new StudentCourseReportDAO(jdbcHelper);
        UserRepository userRepository=new UserDAO(jdbcHelper);

        //services
        JwtService jwtService = new JwtService();
        jersey.register(new JwtAuthFilter(jwtService.getKey()));

        CourseService courseService = new CourseServiceImpl(courseRepository, instructorRepository);
        InstructorService instructorService = new InstructorServiceImpl(instructorRepository);
        StudentService studentService = new StudentServiceImpl(studentRepository);
        StudentCourseService studentCourseService = new StudentCourseServiceImpl( studentCourseRepository);
        StudentCourseReportService studentCourseReportService=new StudentCourseReportServiceImpl(studentCourseReportRepository);
        UserService userService=new UserServiceImpl(userRepository, jwtService);

        //resources
        StudentResource studentResource = new StudentResource(studentService);
        InstructorResource instructorResource = new InstructorResource(instructorService);
        CourseResource courseResource = new CourseResource(courseService);
        StudentCourseResource studentCourseResource = new StudentCourseResource(studentCourseService);
        StudentCourseReportResource studentCourseReportResource = new StudentCourseReportResource(studentCourseReportService);
        UserResource userResource=new UserResource(userService);

        //registers
        jersey.register(studentResource);
        jersey.register(instructorResource);
        jersey.register(courseResource);
        jersey.register(studentCourseResource);
        jersey.register(studentCourseReportResource);
        jersey.register(userResource);
    }
}