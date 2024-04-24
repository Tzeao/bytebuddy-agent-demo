//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jas.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Listener.StreamWriting;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public Main() {
    }

    public static void premain(String args, Instrumentation inst) throws Exception {
        launch(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) throws Exception {
        launch(args, inst);
    }

    private static void launch(String args, Instrumentation inst) throws Exception {
        System.out.println("rainbow-brackets-2024.2.1 cracked plugin");
        AgentBuilder agentBuilder = (new AgentBuilder.Default()).ignore(ElementMatchers.none()).with(RedefinitionStrategy.REDEFINITION).with(StreamWriting.toSystemError().withErrorsOnly()).with(StreamWriting.toSystemOut().withTransformationsOnly()).with(new DumpClassListener());
        byPassJavaAgent(agentBuilder, inst);
        byPassLicense(agentBuilder, inst);
    }

    private static void byPassJavaAgent(AgentBuilder agentBuilder, Instrumentation inst) {
        agentBuilder.type(ElementMatchers.named("com.intellij.diagnostic.VMOptions")).transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
            return builder.visit(Advice.to(VMOptionsInterceptor.class).on(ElementMatchers.named("getUserOptionsFile")));
        }).installOn(inst);
        agentBuilder.type(ElementMatchers.named("com.janetfilter.core.utils.StringUtils")).transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> {
            return builder.visit(Advice.to(StringUtilsInterceptor.class).on(ElementMatchers.named("isEmpty")));
        }).installOn(inst);
        agentBuilder.type(ElementMatchers.named("java.lang.Class")).transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
            return builder.visit(Advice.to(ClassForNameInterceptor.class).on(ElementMatchers.named("forName")));
        }).installOn(inst);
    }

    private static void byPassLicense(AgentBuilder agentBuilder, Instrumentation inst) {
        agentBuilder.type(ElementMatchers.named("com.intellij.ui.LicensingFacade")).transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> {
            return builder.visit(Advice.to(LicenseExpirationInterceptor.class).on(ElementMatchers.named("getLicenseExpirationDate")));
        }).installOn(inst);
    }

    public static class LicenseExpirationInterceptor {
        public LicenseExpirationInterceptor() {
        }

        @OnMethodExit
        public static void exit(@Return(readOnly = false) Date ret) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(5, 50);
            ret = calendar.getTime();
        }
    }

    public static class ReturnBoolInterceptor {
        public ReturnBoolInterceptor() {
        }

        @OnMethodExit
        public static void interceptor(@Return(readOnly = false) boolean ret) {
            try {
                ret = false;
            } catch (Exception var2) {
            }

        }
    }

    public static class ClassForNameInterceptor {
        public ClassForNameInterceptor() {
        }

        @OnMethodEnter
        public static void interceptorBefore(@AllArguments Object[] args, @Origin("#m") String methodName) {
            if ("jdk.internal.org.objectweb.asm.Type".equals(args[0])) {
                throw new RuntimeException("asm 用来修改字节码的");
            }
        }
    }

    public static class VMOptionsInterceptor {
        public VMOptionsInterceptor() {
        }

        @OnMethodExit
        public static void interceptor(@Return(readOnly = false) Path ret) {
            try {
                if ((new Throwable()).getStackTrace()[2].getClassName().startsWith("jdk.internal.reflect")) {
                    String fileName = (String)Class.forName("com.intellij.diagnostic.VMOptions").getDeclaredMethod("getFileName").invoke((Object)null);
                    String location = (String)Class.forName("com.intellij.openapi.application.PathManager").getDeclaredMethod("getBinPath").invoke((Object)null);
                    Paths.get(location, fileName);
                }
            } catch (Exception var3) {
            }

        }
    }

    public static class StringUtilsInterceptor {
        public StringUtilsInterceptor() {
        }

        @OnMethodEnter
        public static void interceptorBefore(@AllArguments Object[] args, @Origin("#m") String methodName) {
            if ("isEmpty".equals(methodName)) {
                Object arg = args[0];
                if (arg != null && arg.toString().isEmpty() && !(new Throwable()).getStackTrace()[2].getClassName().startsWith("com.janetfilter.")) {
                    throw new RuntimeException("fuck you");
                }
            }

        }
    }
}
