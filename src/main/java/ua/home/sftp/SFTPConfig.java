package ua.home.sftp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

/**
 * @author eugene on 26.10.16.
 */
@Configuration
@Setter
@Getter
public class SFTPConfig {


    private DefaultSftpSessionFactory sftp;




//    private String host;
//    private int port;
//    private String username;
//    private String password;
//    private boolean allowUnknownKey;
//    private int timeout;

    private boolean enableSessionCache;


    @ConfigurationProperties(prefix = "sftp")
    @Bean
    public DefaultSftpSessionFactory ftpsSessionFactory() {

        //        sf.setHost(host);
//        sf.setPort(port);
//        sf.setUser(username);
//        sf.setPassword(password);
//        sf.setAllowUnknownKeys(allowUnknownKey);
//        sf.setTimeout(timeout);
//        sf.setEnableDaemonThread(true);

//        if (enableSessionCache) {
//            return cachingSessionFactory(sf);
//        }

        return new DefaultSftpSessionFactory();
    }

    public SessionFactory cachingSessionFactory(SessionFactory sessionFactory) {


        return new CachingSessionFactory(ftpsSessionFactory());

    }

}
