package xinrui.cloud.domain.Interfaces;

import java.util.List;

public interface IUser {

	public String getName();

	public Long getId();

	public List<? extends IRole> getRoles();

	public void setRoles(List<? extends IRole> roles);

}
