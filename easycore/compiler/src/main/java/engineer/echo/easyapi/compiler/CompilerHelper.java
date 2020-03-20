package engineer.echo.easyapi.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import engineer.echo.easyapi.annotation.EasyJobHelper;
import engineer.echo.easyapi.annotation.JobCallback;


final class CompilerHelper {

    static boolean createMetaInfoFile(Filer filer, String appId, String uniqueId, String className) {
        // appId
        FieldSpec fieldAppId = FieldSpec.builder(String.class, "APP_ID")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", appId)
                .build();

        // metaInfo
        FieldSpec metaInfo = FieldSpec.builder(String.class, "metaInfo")
                .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$S", className)
                .build();

        // uniqueId
        FieldSpec fieldId = FieldSpec.builder(String.class, "uniqueId")
                .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
                .initializer("$S", uniqueId)
                .build();

        // class
        TypeSpec classType = TypeSpec.classBuilder(EasyJobHelper.CLASS)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(metaInfo)
                .addField(fieldAppId)
                .addField(fieldId)
                .build();
        // file
        JavaFile javaFile = JavaFile.builder(EasyJobHelper.generatePackage(uniqueId), classType)
                .addFileComment("This codes are generated by EasyApi automatically. Do not modify!")
                .build();
        try {
            javaFile.writeTo(filer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    static String createRetrofitApi(Filer filer, TypeElement interfaceElement) {
        try {
            String className = interfaceElement.getQualifiedName().toString();
            String simpleName = interfaceElement.getSimpleName().toString();
            String packageName = className.replace("." + simpleName, "");

            // interface
            TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(simpleName + "Retrofit")
                    .addModifiers(Modifier.PUBLIC);
            List<? extends Element> elements = interfaceElement.getEnclosedElements();
            for (Element element : elements) {
                if (element.getKind() == ElementKind.METHOD && element instanceof Symbol.MethodSymbol) {
                    String methodName = element.getSimpleName().toString();
                    Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) element;
                    AnnotationSpec specGet = AnnotationSpec.builder(ClassName.get("retrofit2.http", "GET"))
                            .addMember("value", "$S",
                                    EasyJobHelper.generateRetrofitPath(className, methodName))
                            .build();

                    ClassName liveDataClass = ClassName.get("androidx.lifecycle", "LiveData");
                    TypeName returnClass = TypeName.get(methodSymbol.getReturnType());
                    ParameterizedTypeName returnType = ParameterizedTypeName.get(liveDataClass, returnClass);

                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                            .addModifiers(methodSymbol.getModifiers())
                            .addAnnotation(specGet)
                            .returns(returnType);

                    List<Symbol.VarSymbol> varSymbols = methodSymbol.getParameters();
                    AnnotationSpec.Builder specHeader = AnnotationSpec.builder(ClassName.get("retrofit2.http", "Headers"));
                    int paramSize = varSymbols.size();
                    for (int i = 0; i < paramSize; i++) {
                        Symbol.VarSymbol var = varSymbols.get(i);
                        String queryName = var.name.toString();
                        AnnotationSpec specQuery = AnnotationSpec
                                .builder(ClassName.get("retrofit2.http", "Query"))
                                .addMember("value", "$S", queryName)
                                .build();
                        TypeName queryType = TypeName.get(var.type);
                        String queryTypeStr = queryType.toString();
                        boolean isJobCallback = queryTypeStr.equals(JobCallback.class.getName());
                        if (isJobCallback && i != paramSize - 1) {
                            return "EasyApi：require one JobCallback at most and put it at last position.";
                        }
                        if (!isJobCallback) {
                            methodBuilder.addParameter(ParameterSpec.builder(queryType, queryName)
                                    .addAnnotation(specQuery)
                                    .build());
                        }
                        specHeader.addMember("value", "$L",
                                CodeBlock.builder().add("$S", queryName + ":" + queryTypeStr).build());
                    }
                    methodBuilder.addAnnotation(specHeader.build());
                    interfaceBuilder.addMethod(methodBuilder.build());
                }
            }
            TypeSpec interfaceType = interfaceBuilder.build();

            // file
            JavaFile javaFile = JavaFile.builder(packageName, interfaceType)
                    .addFileComment("This codes are generated by EasyApi automatically. Do not modify!")
                    .build();
            try {
                javaFile.writeTo(filer);
                return null;
            } catch (IOException e) {
                return "EasyApi writeFailed " + e.getMessage();
            }
        } catch (Exception e) {
            return "EasyApi classFailed " + e.toString();
        }
    }
}