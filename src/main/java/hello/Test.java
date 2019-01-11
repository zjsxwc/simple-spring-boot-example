package hello;

import org.springframework.stereotype.Component;

@Component
public class Test {

    public String say(String x){
        System.out.println(x);
        return x;
    }

}
