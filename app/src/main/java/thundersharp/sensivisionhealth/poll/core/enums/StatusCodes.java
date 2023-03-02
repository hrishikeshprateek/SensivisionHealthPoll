package thundersharp.sensivisionhealth.poll.core.enums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@IntDef({StatusCodes.STATUS_PRIVATE,
        StatusCodes.STATUS_PUBLIC,
        StatusCodes.STATUS_CLOSED})
public @interface StatusCodes {
    int STATUS_PRIVATE = 0;
    int STATUS_PUBLIC = 1;
    int STATUS_CLOSED = 2;
}
