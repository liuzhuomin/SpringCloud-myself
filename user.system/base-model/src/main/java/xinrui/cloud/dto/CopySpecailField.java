package xinrui.cloud.dto;

import xinrui.cloud.domain.TreeEntity;

public abstract class CopySpecailField {
	public abstract <E extends TreeEntity<E>, T> void copy(T t, TreeEntity<E> e);
}