package org.futurepages.components;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.DateField;
import org.futurepages.core.locale.LocaleManager;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarDateField extends DateField {

	public CalendarDateField(){
		super();
		this.setPropertyDataSource(new CalendarProperty());
		this.setConverter(new DateToCalendarConverter());
		this.setFormatDateStyle(FormatStyle.MEDIUM);
	}

	public void setFormatDateStyle(FormatStyle formatStyle){
		String datePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(formatStyle, formatStyle, IsoChronology.INSTANCE, LocaleManager.getDefaultLocale());
		this.setDateFormat(datePattern);
	}

	public CalendarDateField(String caption){
		this();
		this.setCaption(caption);
	}

	public CalendarDateField(String caption, Calendar value) {
		this(caption);
		this.getPropertyDataSource().setValue(value);
	}

	private static class CalendarProperty implements Property<Calendar> {

            private Calendar value;

            @Override
            public Calendar getValue() {
                return value;
            }

            @Override
            public void setValue(Calendar newValue) throws ReadOnlyException {
                value = newValue;
            }

            @Override
            public Class getType() {
                return Calendar.class;
            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public void setReadOnly(boolean newStatus) {

            }
        }

	private static class DateToCalendarConverter implements Converter<Date, Calendar> {
		@Override
		public Calendar convertToModel(Date value, Class<? extends Calendar> targetType, Locale locale) throws
				Converter.ConversionException {
			if (value == null) {
				return null;
			}
			Calendar newCal = Calendar.getInstance(locale);
			newCal.setTime(value);
			return newCal;
		}

		@Override
		public Date convertToPresentation(Calendar value, Class<? extends Date> targetType, Locale locale) throws
				Converter.ConversionException {
			return (value == null) ? null : value.getTime();
		}

		@Override
		public Class<Calendar> getModelType() {
			return Calendar.class;
		}

		@Override
		public Class<Date> getPresentationType() {
			return Date.class;
		}
	}
}
