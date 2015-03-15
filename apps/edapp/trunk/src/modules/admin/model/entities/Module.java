package modules.admin.model.entities;

import modules.admin.model.dao.RoleDao;
import org.futurepages.core.auth.DefaultModule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Module implements DefaultModule, Serializable{
    
    @Id
    @Column(length=30,nullable=false)
    private String moduleId;
    
    @Column(length=100,nullable=false)
    private String title;
    
    @Column(length=20,nullable=false)
    private String smallTitle;
    
	@ManyToMany
    @JoinTable(name="admin_profile_module",
				joinColumns=@JoinColumn(name="moduleId"),
				inverseJoinColumns=@JoinColumn(name="profileId"))
     private Collection<Profile> profiles;

    public Module() {
    }
    
    public Module(String moduleId) {
        this.moduleId = moduleId;
    }

	@Transient
	private List<Role> roles;

	public List<Role> getRoles(){
		if(roles == null){
			roles = RoleDao.listByModule(this.getModuleId());
		}
		return roles;
	}

	public List<Role> getRoles(User user){
		return user.getRoles().stream().filter(role -> role.getModule().equals(this)).collect(Collectors.toList());
	}

    @Override
    public String toString(){
        return this.moduleId;
    }

	public Collection<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(Collection<Profile> profiles) {
		this.profiles = profiles;
	}

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

	@Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	@Override
    public String getSmallTitle() {
        return smallTitle;
    }

    public void setSmallTitle(String smallTitle) {
        this.smallTitle = smallTitle;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Module other = (Module) obj;
		if ((this.moduleId == null) ? (other.moduleId != null) : !this.moduleId.equals(other.moduleId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 23 * hash + (this.moduleId != null ? this.moduleId.hashCode() : 0);
		return hash;
	}
	
	
}