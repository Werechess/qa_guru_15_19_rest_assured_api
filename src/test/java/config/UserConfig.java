package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/user.properties")
public interface UserConfig extends Config {

    @Key("username")
    String username();

    @Key("password")
    String password();

    @Key("token")
    String token();
}
