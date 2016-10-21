package org.futurepages.menta.core.input;

import org.futurepages.menta.exceptions.InputException;
import org.futurepages.menta.util.InjectionUtils;
import org.futurepages.util.Is;
import org.futurepages.util.brazil.BrazilCalendarUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractInput implements Input {

	/**
     * Calls getStringValue() and tries to convert the string to boolean.
     */
	@Override
    public boolean getBooleanValue(String name) {

        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return false;
        }

        value = value.trim();

        if (value.equalsIgnoreCase("false")) return false;

        if (value.equalsIgnoreCase("true") || value.equals("on")) return true;

        int x = -1;

        try {
            x = Integer.parseInt(value);

            if (x == 0) return false;
            else if (x == 1) return true;

        } catch(Exception e) { }

        throw new InputException("Could not convert input to boolean: " + name + " (" + value + ")");
    }

	@Override
	public Calendar getCalendarValue(String name){
		if(getValue(name) instanceof Calendar){
			return (Calendar) getValue(name);
		}
        String value = getStringValue(name);
		return BrazilCalendarUtil.viewDateTime(value);
	}



	@Override
	public boolean getBooleanValue(String name, boolean def) {

        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return def;
        }
        
        return getBooleanValue(name);
        
    }
	
	@Override
	public Date getDate(String name) {
		return getDate(name, null, DateFormat.SHORT);
	}
	
	@Override
	public Date getDate(String name, int style) {
		return getDate(name, null, style);
	}

	@Override
	public Date getDate(String name, String pattern) {
		return getDate(name, pattern, -1);
	}
	
	private Date getDate(String name, String pattern, int style) {
		
		// first check whether we already have an Date...
		
		Object obj = getValue(name);
		
		if (obj instanceof Date) {
			
			return (Date) obj;
		}
		
		// if not parse...
		
		String date = getStringValue(name);
		
		if(Is.empty(date)){
			return null;
		}
		
		date = date.trim();
		
		try {
			
			DateFormat df;
			if(pattern != null){
				
				df = new SimpleDateFormat(pattern);
				
			} else if(style != -1){
				
				df = DateFormat.getDateInstance(style, getLocale());
				
			} else {
				
				throw new IllegalStateException("Should never be here!");
				
			}
			return df.parse(date);
			
		} catch(ParseException e) {
			
            throw new InputException("Could not convert input to date: " + name + " (" + date + ")");
		}
	}
	
	protected abstract Locale getLocale();
    
    
    /**
     * Calls getStringValue() and tries to convert the string to a double.
     */
	@Override
    public double getDoubleValue(String name) {
        
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return -1;
        }
        
        value = value.trim();

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new InputException("Could not convert input to double: "
                    + name + " (" + value + ")");
        }
    }
    
	@Override
    public double getDoubleValue(String name, double def) {
        
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            
            return def;
        }
        
        return getDoubleValue(name);
    }
    
    /**
     * Calls getStringValue() and tries to convert the string to a float.
     */
	@Override
    public float getFloatValue(String name) {
        
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return -1;
        }
        
        value = value.trim();

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new InputException("Could not convert input to float: "
                    + name + " (" + value + ")");
        }
    }
    
	@Override
    public float getFloatValue(String name, float def) {
        
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {

            return def;
        }
        
        return getFloatValue(name);

    }
    
	@Override
    public int getIntValue(String name) {
        String value = getStringValue(name);
        if (value == null || value.trim().equals("")) {
            //throw new NullPointerException("No value defined for " + name);
            return -1;
        }
        
        value = value.trim();
        
        try {
            return Integer.parseInt(value);
        } catch(Exception e) {
            throw new InputException("Could not convert input to number: " + name + " (" + value + ")");
        }
    }
    
	@Override
    public int getIntValue(String name, int def) {
        
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            return def;
        }
        
        return getIntValue(name);

    }
    
	@Override
    public int[] getIntValues(String name) {
		String[] values = getStringValues(name);

		if (values == null || values.length == 0) {
			return null;
		}

		int [] ret = new int[values.length];

		for(int i = 0; i <values.length; i++) {
			
			try {
				
				if (values[i] == null) throw new InputException("Cannot convert null value: " + name);
				
				ret[i] = Integer.parseInt(values[i].trim());
				
			} catch(NumberFormatException e) {
				
				throw new InputException("Could not convert input to numbers: " + name);
				
			}
		}
		return ret;
	}
    
    /**
     * Calls getStringValue() and tries to convert the string to a long.
     */
	@Override
    public long getLongValue(String name) {
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            // throw new NullPointerException(name + " is not defined");
            return -1;
        }

        value = value.trim();
        
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InputException("Could not convert input to long: "
                    + name + " (" + value + ")");
        }
    }

	public long getLongValue(String name, long def) {
        String value = getStringValue(name);

        if (value == null || value.trim().equals("")) {
            return def;
        }
        
        return getLongValue(name);
    }

	
	public <E> E getObject(Class<? extends E> klass) {

		return getObject(getInstance(klass), null);

	}

	public <E> E getObject(Class<? extends E> klass, String prefix) {

		return getObject(getInstance(klass), prefix, true, true, true);

	}
	
	private <E> E getInstance(Class<? extends E> klass) {
		
		try {
			
			return klass.newInstance();
			
		} catch(Exception e) {
			
			throw new InputException(e);
		}
	}
	
	public <E> E getObject(E bean) {
		
		return getObject(bean, null);
		
	}
	
	public <E> E getObject(E bean, String prefix) {
		
		return getObject(bean, prefix, true, true, true);
	}

	public <E> E getObject(E target, String prefix, boolean tryField, boolean tryToConvert, boolean convertBoolean) {

		try {

			InjectionUtils.getObject(target, this, getLocale(), tryField, prefix, tryToConvert, convertBoolean);

			return target;

		} catch (Exception e) {

			throw new InputException(e);
		}
	}
	
	public <E extends Enum<E>> E getEnum(String key, Class<E> enumClass) {
		String value = getStringValue(key);
		if (value == null)
			return null;
		try {
			return Enum.valueOf(enumClass, value);
		} catch (Exception e) {
			return null;
		}
	}

}
