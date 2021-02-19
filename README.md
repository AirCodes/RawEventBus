# RawEventBus
A simple eventbus.It should be more efficient than the ones using java-reflect to do invoking job.
It won't take so much ROM so I don't push a release.
# Usage:
0.Import org.objectweb.asm to ur proj.
1.Put all the .java in your application.
2.Enjoy!
# About the efficiency
Method handle is a new feature in java7.
It provides a faster invocation service.
You can also check out these article below.
https://www.zhihu.com/question/47764575
And the comparison about reflect-asm and reflect.
https://www.cnblogs.com/tohxyblog/p/8661090.html
