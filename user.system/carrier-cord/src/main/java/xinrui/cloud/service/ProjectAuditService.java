package xinrui.cloud.service;

import xinrui.cloud.domain.ProjectAudit;
import xinrui.cloud.domain.dto.ProjectAuditDto;

public interface ProjectAuditService extends BaseService<ProjectAudit> {
    /**
     * 項目审核
     * @param proAuditDto
     */
    void projectAudit(ProjectAuditDto proAuditDto);
}
