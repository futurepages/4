package modules.admin.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import modules.admin.dao.RoleDao;

@Entity
@Table(name="admin_module")
public class Module implements Serializable{
    
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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