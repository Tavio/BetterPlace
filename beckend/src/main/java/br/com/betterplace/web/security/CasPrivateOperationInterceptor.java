package br.com.betterplace.web.security;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CasPrivateOperationInterceptor {

//    private AuthMap getAuthMap() {
//        return (AuthMap) ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//                .getRequest().getAttribute(CasPrivateFilter.CAS_AUTH_MAP_ATTRIBUTE);
//    }

//    private void doCheck(CasPrivateOperation privateOperation) {
//        AuthMap authMap = this.getAuthMap();
//        if (authMap == null
//                || authMap.getSystems() == null
//                || authMap.getSystems().get(privateOperation.systemKey()) == null
//                || authMap.getSystems().get(privateOperation.systemKey()).getProfile() == null
//                || authMap.getSystems().get(privateOperation.systemKey()).getProfile().getOperations() == null
//                || !authMap.getSystems().get(privateOperation.systemKey()).getProfile().getOperations().containsKey(privateOperation.operationKey())) {
//            throw new CasPermissionDeniedException();
//        }
//    }

//    @Before("execution(public * br.com.nexusedge.web.control.*.*(..)) && @annotation(privateOperation)")
//    public void check(CasPrivateOperation privateOperation) {
//        this.doCheck(privateOperation);
//    }
}
