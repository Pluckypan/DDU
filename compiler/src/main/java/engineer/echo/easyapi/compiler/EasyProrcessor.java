package engineer.echo.easyapi.compiler;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import engineer.echo.easyapi.annotation.JobServer;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EasyProrcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private String appId = "engineer.echo.easyapi";
    private Set<String> supportAnnos = new LinkedHashSet<>();
    private HashMap<String, String> metaInfo = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        messager = env.getMessager();
        supportAnnos.add(JobServer.class.getCanonicalName());
        Map<String, String> options = env.getOptions();
        if (options.containsKey("easyapi.appId")) {
            appId = options.get("easyapi.appId");
            if (appId == null || appId.length() == 0) {
                appId = "engineer.echo.easyapi";
            }
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        metaInfo.clear();
        for (Element element : env.getElementsAnnotatedWith(JobServer.class)) {
            if (element.getKind() == ElementKind.CLASS && element instanceof TypeElement) {
                JobServer jobServer = element.getAnnotation(JobServer.class);
                String className = ((TypeElement) element).getQualifiedName().toString();
                if (!metaInfo.containsKey(jobServer.uniqueId())) {
                    if (CompilerHelper.createMetaInfoFile(filer, appId, jobServer.uniqueId(), className)) {
                        metaInfo.put(jobServer.uniqueId(), className);
                    } else {
                        String message = className + " generate failed for " + " uniqueId  " + jobServer.uniqueId();
                        messager.printMessage(Diagnostic.Kind.ERROR, message);
                        throw new IllegalArgumentException(message);
                    }
                } else {
                    String message = jobServer.uniqueId() + " has bind to " + metaInfo.get(jobServer.uniqueId()) + " please set an uniqueId to " + className;
                    messager.printMessage(Diagnostic.Kind.ERROR, message);
                    throw new IllegalArgumentException(message);
                }
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportAnnos;
    }
}
