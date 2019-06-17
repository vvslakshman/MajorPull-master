package majorpull.com.majorpull.user.DataModel;

/**
 * Created by user102 on 3/23/18.
 */

public class NotificatonUserPojo {


    public int profileImage;
    public String description;
    public String title;

    public NotificatonUserPojo(int userimage, String title,String detail) {

        this.profileImage=userimage;
        this.title=title;
        this.description=detail;
    }
}
