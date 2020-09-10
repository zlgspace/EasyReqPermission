package com.zlgspace.easyreqpermission.compile;

import com.google.auto.service.AutoService;
import com.zlgspace.apt.base.BuildClass;
import com.zlgspace.easyreqpermission.annotation.ForbidPermission;
import com.zlgspace.easyreqpermission.annotation.NeedPermission;
import com.zlgspace.easyreqpermission.annotation.ProclaimPermission;
import com.zlgspace.easyreqpermission.annotation.RefusePermission;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class EasyReqPermissionProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private Filer mFiler;

    private  HashMap<String ,StringBuilder> executionUnitInit = new HashMap<>();
    private  HashMap<String ,StringBuilder> proclaimPermissions = new HashMap<>();
    private  HashMap<String ,StringBuilder> refusePermissions = new HashMap<>();
    private  HashMap<String ,StringBuilder> forbidPermissions = new HashMap<>();
    private  HashMap<String ,StringBuilder> gotPermissions = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.add(NeedPermission.class.getCanonicalName());
        set.add(ProclaimPermission.class.getCanonicalName());
        set.add(RefusePermission.class.getCanonicalName());
        set.add(ForbidPermission.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> needPermissions = roundEnvironment.getElementsAnnotatedWith(NeedPermission.class);
        HashMap<String ,BuildClass> clzMap = new HashMap<>();
        for(Element element:needPermissions){
            parserNeedPermissionJava(element,clzMap);
        }

        Set<? extends Element> proclaimPermission = roundEnvironment.getElementsAnnotatedWith(ProclaimPermission.class);
        for(Element element:proclaimPermission){
            parserProclaimPermission(element);
        }

        Set<? extends Element> refusePermission = roundEnvironment.getElementsAnnotatedWith(RefusePermission.class);
        for(Element element:refusePermission){
            parserRefusePermission(element);
        }

        Set<? extends Element> forbidPermission = roundEnvironment.getElementsAnnotatedWith(ForbidPermission.class);
        for(Element element:forbidPermission){
            parserForbidPermission(element);
        }

        for(String key:executionUnitInit.keySet()){
            BuildClass buildClass = clzMap.get(key);
            if(buildClass==null)
                continue;
            StringBuilder builder = executionUnitInit.get(key);
            builder.append("}\n");
            buildClass.appendMethod(builder.toString());
        }


        for(String key:proclaimPermissions.keySet()){
            BuildClass buildClass = clzMap.get(key);
            if(buildClass==null)
                continue;
            StringBuilder builder = proclaimPermissions.get(key);
            builder.append("}\n");
            builder.append("return true;\n");
            builder.append("}\n");
            buildClass.appendMethod(builder.toString());
        }

        for(String key:refusePermissions.keySet()){
            BuildClass buildClass = clzMap.get(key);
            if(buildClass==null)
                continue;
            StringBuilder builder = refusePermissions.get(key);
            builder.append("}\n");
            builder.append("}\n");
            buildClass.appendMethod(builder.toString());
        }

        for(String key:forbidPermissions.keySet()){
            BuildClass buildClass = clzMap.get(key);
            if(buildClass==null)
                continue;
            StringBuilder builder = forbidPermissions.get(key);
            builder.append("}\n");
            builder.append("}\n");
            buildClass.appendMethod(builder.toString());
        }

        for(String key:gotPermissions.keySet()){
            BuildClass buildClass = clzMap.get(key);
            if(buildClass==null)
                continue;
            StringBuilder builder = gotPermissions.get(key);
            builder.append("}\n");
            builder.append("}\n");
            buildClass.appendMethod(builder.toString());
        }


        saveJave(clzMap);

        return false;
    }

    private void parserNeedPermissionJava(Element element, HashMap<String , BuildClass> clzMap){
        if(element.getKind() != ElementKind.METHOD){
            return;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE))
            return;

        NeedPermission np = element.getAnnotation(NeedPermission.class);
        String clzSimpleName = element.getEnclosingElement().getSimpleName().toString();
        String permissions[] = np.permissions();
        String identifier = np.identifier();
        String methodName = element.getSimpleName().toString();
        String pkgName = elementUtils.getPackageOf(element).toString();
        ExecutableElement methodElement =  (ExecutableElement)element;

        if(identifier==null||identifier.length()==0){
            identifier = "";
            for(String p:permissions){
                identifier+=p;
            }
        }

        String clzKey = pkgName+"."+clzSimpleName;

        BuildClass buildClass = null;
        if(!clzMap.containsKey(clzKey)){
            buildClass = new BuildClass();
            buildClass.setPackage(pkgName);
            buildClass.setName(clzSimpleName+"_ReqPermission");
            buildClass.setClzModifiers("public class ");
            buildClass.setClzSuffix(" extends EasyReqPermissionHandler");

            buildClass.appendImport("import java.util.HashMap;\n");
            buildClass.appendImport("import com.zlgspace.easyreqpermission.ExecutionUnit;\n");
            buildClass.appendImport("import com.zlgspace.easyreqpermission.PermissionTag;\n");
            buildClass.appendImport("import com.zlgspace.easyreqpermission.EasyReqPermissionHandler;\n");
            buildClass.appendImport("import com.zlgspace.easyreqpermission.utils.PermissionUtils;\n");

            buildClass.appendMethod("public "+clzSimpleName+"_ReqPermission( "+clzSimpleName+" obj ){\n");
            buildClass.appendMethod("bindActivity(obj);\n");
            buildClass.appendMethod("init();\n");
            buildClass.appendMethod("}\n");
            clzMap.put(clzKey,buildClass);
        }else{
            buildClass = clzMap.get(clzKey);
        }


        buildClass.appendMethod("public void "+methodName+"(){\n");
        buildClass.appendMethod("curExecutionUnit = executionUnitList.get(\""+methodName+"\");\n");
        buildClass.appendMethod("if(curExecutionUnit==null) return;\n");
        buildClass.appendMethod("curExecutionUnit.bindEasyReqPermissionHandler(this);\n");
        buildClass.appendMethod("curExecutionUnit.execute();\n");
        buildClass.appendMethod("}\n");

        StringBuilder executionUnitInitBuilder = null;
        if(!executionUnitInit.containsKey(clzKey)) {
            StringBuilder unitInitBuilder = new StringBuilder();

            unitInitBuilder.append("private void init(){\n");
            executionUnitInit.put(clzKey, unitInitBuilder);
        }

        executionUnitInitBuilder = executionUnitInit.get(clzKey);

        String permissionTagName = methodName+"_permissionTags";
        executionUnitInitBuilder.append("PermissionTag "+permissionTagName+"[] = new PermissionTag["+permissions.length+"];\n");
        int index = 0;
        for(String p:permissions){
            executionUnitInitBuilder.append(permissionTagName+"["+(index++)+"] = new PermissionTag(\""+p+"\");\n");
        }
        String unitName = methodName+"_Unit";
        executionUnitInitBuilder.append("ExecutionUnit "+unitName+" = new ExecutionUnit(\""+methodName+"\","+permissionTagName+");\n");
        executionUnitInitBuilder.append(unitName+".setName(\""+identifier+"\");\n");
        executionUnitInitBuilder.append("addExecutionUnit("+unitName+");\n");


        StringBuilder gotPermissionBuilder = null;
        if(!gotPermissions.containsKey(clzKey)){
            StringBuilder gotBuilder = new StringBuilder();
            gotBuilder.append("protected void gotPermissions(String[] permissions){\n");
            gotBuilder.append("if(curExecutionUnit == null) return;\n");
            gotBuilder.append("String methodName = curExecutionUnit.getTargetMethodId();\n");
            gotBuilder.append("switch(methodName){\n");
            gotPermissions.put(clzKey,gotBuilder);
        }

        gotPermissionBuilder = gotPermissions.get(clzKey);
        gotPermissionBuilder.append("case \""+methodName+"\":\n");
        gotPermissionBuilder.append("(("+clzSimpleName+")bindObj)."+methodName+"();\n");
        gotPermissionBuilder.append("break;\n");

    }

    private void parserProclaimPermission(Element element){
        if(element.getKind() != ElementKind.METHOD){
            return;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE))
            return;

        ProclaimPermission np = element.getAnnotation(ProclaimPermission.class);
        String clzSimpleName = element.getEnclosingElement().getSimpleName().toString();
        String permissions[] = np.permissions();
        String identifier = np.identifier();
        String methodName = element.getSimpleName().toString();
        String pkgName = elementUtils.getPackageOf(element).toString();
        ExecutableElement methodElement =  (ExecutableElement)element;

        if(identifier==null||identifier.length()==0){
            identifier = "";
            for(String p:permissions){
                identifier+=p;
            }
        }

        String clzKey = pkgName+"."+clzSimpleName;

        StringBuilder proclaimPermissionBuilder = null;
        if(!proclaimPermissions.containsKey(clzKey)){
            StringBuilder permissionBuilder = new StringBuilder();
            permissionBuilder.append("protected boolean proclaimPermissions(){\n");
            permissionBuilder.append("if(curExecutionUnit==null) return false;\n");
            permissionBuilder.append("String proclaimMethodId = curExecutionUnit.getProclaimMethodId();\n");
            permissionBuilder.append("if(proclaimMethodId==null||proclaimMethodId.length()==0) return false;\n");
            permissionBuilder.append("switch(proclaimMethodId){\n");

            proclaimPermissions.put(clzKey,permissionBuilder);
        }

        proclaimPermissionBuilder = proclaimPermissions.get(clzKey);

        proclaimPermissionBuilder.append("case \""+methodName+"\":\n");
        proclaimPermissionBuilder.append("(("+clzSimpleName+")bindObj)."+methodName+"();\n");
        proclaimPermissionBuilder.append("break;\n");

//        getExecutionUnitByName
        StringBuilder builder = executionUnitInit.get(clzKey);
        if(builder==null)
            return;
        String unitName = "unit"+identifier.hashCode();
        builder.append("ExecutionUnit "+unitName+" = getExecutionUnitByName(\""+identifier+"\");\n");
        builder.append(unitName+".setProclaimMethodId(\""+methodName+"\");\n");
    }

    private void parserRefusePermission(Element element){
        if(element.getKind() != ElementKind.METHOD){
            return;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE))
            return;

        RefusePermission np = element.getAnnotation(RefusePermission.class);
        String clzSimpleName = element.getEnclosingElement().getSimpleName().toString();
        String permissions[] = np.permissions();
        String identifier = np.identifier();
        String methodName = element.getSimpleName().toString();
        String pkgName = elementUtils.getPackageOf(element).toString();
        ExecutableElement methodElement =  (ExecutableElement)element;

        if(identifier==null||identifier.length()==0){
            identifier = "";
            for(String p:permissions){
                identifier+=p;
            }
        }

        String clzKey = pkgName+"."+clzSimpleName;

        StringBuilder refusePermissionBuilder = null;
        if(!refusePermissions.containsKey(clzKey)){
            StringBuilder temp = new StringBuilder();
            temp.append("protected void refusePermissions(String[] permissions,int[] grantResults){\n");
            temp.append("if(curExecutionUnit==null) return;\n");
            temp.append("String name = curExecutionUnit.getName();\n");
            temp.append("switch(name){\n");
            refusePermissions.put(clzKey,temp);
        }

        refusePermissionBuilder = refusePermissions.get(clzKey);
        refusePermissionBuilder.append("case \""+identifier+"\":\n");
        refusePermissionBuilder.append("(("+clzSimpleName+")bindObj)."+methodName+"();\n");
        refusePermissionBuilder.append("break;");
    }

    private void parserForbidPermission(Element element){
        if(element.getKind() != ElementKind.METHOD){
            return;
        }
        if(element.getModifiers().contains(Modifier.PRIVATE))
            return;

        ForbidPermission np = element.getAnnotation(ForbidPermission.class);
        String clzSimpleName = element.getEnclosingElement().getSimpleName().toString();
        String permissions[] = np.permissions();
        String identifier = np.identifier();
        String methodName = element.getSimpleName().toString();
        String pkgName = elementUtils.getPackageOf(element).toString();
        ExecutableElement methodElement =  (ExecutableElement)element;

        if(identifier==null||identifier.length()==0){
            identifier = "";
            for(String p:permissions){
                identifier+=p;
            }
        }

        String clzKey = pkgName+"."+clzSimpleName;

        StringBuilder refusePermissionBuilder = null;
        if(!forbidPermissions.containsKey(clzKey)){
            StringBuilder temp = new StringBuilder();
            temp.append("protected void forbidPermissions(String[] permissions,int[] grantResults){\n");
            temp.append("if(curExecutionUnit==null) return;\n");
            temp.append("String name = curExecutionUnit.getName();\n");
            temp.append("switch(name){\n");
            forbidPermissions.put(clzKey,temp);
        }

        refusePermissionBuilder = forbidPermissions.get(clzKey);
        refusePermissionBuilder.append("case \""+identifier+"\":\n");
        refusePermissionBuilder.append("(("+clzSimpleName+")bindObj)."+methodName+"();\n");
        refusePermissionBuilder.append("break;");
    }

    private void saveJave( HashMap<String ,BuildClass> clzMap){
        for(String key : clzMap.keySet()){
            BuildClass bc = clzMap.get(key);
            try { // write the file
                JavaFileObject source = mFiler.createSourceFile(bc.getPackage() +"."+ bc.getName());
                Writer writer = source.openWriter();
                writer.write(bc.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // Note: calling e.printStackTrace() will print IO errors
                // that occur from the file already existing after its first run, this is normal
            }
        }
    }
}
