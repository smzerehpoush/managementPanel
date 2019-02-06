package com.nrdc.policeHamrah.helper;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MakeHTML {
    private static final class HTMLStyle extends ToStringStyle {

        HTMLStyle() {
            setFieldSeparator("</td></tr>" + System.getProperty("line.separator") + "<tr><td>");

            setContentStart(System.getProperty("line.separator") +
                    "<tr><td>");

            setFieldNameValueSeparator("</td><td>");

            setContentEnd("</td></tr>" + System.getProperty("line.separator"));

            setArrayContentDetail(true);
            setUseShortClassName(true);
            setUseClassName(false);
            setUseIdentityHashCode(false);
        }

        @Override
        public void appendDetail(StringBuffer buffer, String fieldName, Object value) {
            if (value.getClass().getName().startsWith("java.lang")) {
                super.appendDetail(buffer, fieldName, value);
            } else {
                buffer.append(ReflectionToStringBuilder.toString(value, this));
            }
        }
    }

    static public String makeHTML(Object object) {
        return ReflectionToStringBuilder.toString(object, new HTMLStyle());
    }
}
