package cn.edu.nju.software.fabricservice.config;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 配置文件，
 */
@AllArgsConstructor
@NoArgsConstructor
public class HFConfig {

    private static HFConfig instance;
    @Getter
    @Setter
    List<ChannelConfig> channels;
    @Getter
    @Setter
    List<UserConfig> users;
    @Getter
    @Setter
    CAConfig caConfig;
    @Getter
    @Setter
    String defaultChannel;
    @Getter
    @Setter
    String defaultUser;
    @Setter
    @Getter
    String ccName;
    @Setter
    @Getter
    String ccVersion;


    public static HFConfig newInstance() {
        if (instance == null) {
            instance = new HFConfig();
            loadConfigFile();
        }
        return instance;
    }

    private static void loadConfigFile() {
        Yaml yaml = new Yaml();
        instance = yaml.loadAs(HFConfig.class.getResourceAsStream("/config.yml"),
                HFConfig.class);
    }

    public static void main(String[] args) {
        HFConfig hfConfig = HFConfig.newInstance();
//        Yaml yaml = new Yaml();
//        HFConfig hfConfig = yaml.loadAs(HFConfig.class.getResourceAsStream("/config.yml"),
//                HFConfig.class);
        System.out.println(new Gson().toJson(hfConfig));
    }


}
