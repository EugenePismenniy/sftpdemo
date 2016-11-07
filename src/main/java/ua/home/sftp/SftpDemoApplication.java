package ua.home.sftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import ua.home.dbf.DBFReader;

import java.io.File;
import java.io.FileOutputStream;

@SpringBootApplication
@EnableConfigurationProperties
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

			System.out.printf("listName.length = '%s'\n", listNames != null ? ""+listNames.length : "null");

			File tmpdbf = File.createTempFile("tmpdbf", "");

			System.out.println(tmpdbf.getAbsolutePath());





			for (String name : listNames) {

				if (StringUtils.endsWithIgnoreCase(name, ".dbf")) {

					String filePath = path + name;

					System.out.printf("\tname = '%s'\n", filePath);


					try (FileOutputStream outputStream = FileUtils.openOutputStream(tmpdbf)) {
						session.read(filePath, outputStream);
						outputStream.flush();
					}


						DBFReader.dbfProcessing(tmpdbf.getAbsolutePath());
				}

			}


		} catch (Exception e) {
			log.error("Error ftp!", e);
		}

		//cachingSessionFactory.destroy();

	}
}
