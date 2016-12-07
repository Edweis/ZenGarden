package models.tools;

public interface BuilderFactory<T> {

	public abstract String validate();

	public abstract T replace(T object);

}
