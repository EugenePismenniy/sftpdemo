package ua.home.sftp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;

@SpringBootApplication
@EnableConfigurationProperties//(SFTPConfig.class)
@Slf4j
public class SftpDemoApplication implements CommandLineRunner {


	@Autowired
	private SessionFactory sessionFactory;


	@Value("${sftp.path}")
	private String path;

	public static void main(String[] args) throws ClassNotFoundException {

		new SpringApplicationBuilder(SftpDemoApplication.class)
				.web(false)
				.run(args);
	}

	@Override
	public void run(String... strings) throws Exception {


		try (Session session = sessionFactory.getSession()) {

			String[] listNames = session.listNames(path);

			for (String listName : listNames) {
				System.out.println(listName);
			}


		} catch (Exception e) {
			log.error("Error ftp!", e);
		}

		//cachingSessionFactory.destroy();

	}
}
