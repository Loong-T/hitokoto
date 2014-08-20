package in.nerd_is.hitokoto.api;

import java.util.Date;

/**
 * Created by Zheng Xuqiang on 2014/8/19 0019.
 */
public class HitokotoVo {
    public long id;
    public String hitokoto;
    public String cat;
    public String catname;
    public String author;
    public String source;
    public int like;
    public Date date;
    public String error;

    public HitokotoVo() {
    }

    public HitokotoVo(String error) {
        this.error = error;
    }
}
