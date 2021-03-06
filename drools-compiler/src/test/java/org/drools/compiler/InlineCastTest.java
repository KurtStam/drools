package org.drools.compiler;

import org.junit.Test;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class InlineCastTest extends CommonTestMethodBase {

    @Test
    public void testInlineCast() throws Exception {
        String str = "import org.drools.compiler.*;\n" +
                "rule R1 when\n" +
                "   Person( name == \"mark\", address#LongAddress.country == \"uk\" )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        Person mark2 = new Person("mark");
        ksession.insert(mark2);

        Person mark3 = new Person("mark");
        mark3.setAddress(new Address());
        ksession.insert(mark3);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineCastWithBinding() throws Exception {
        String str = "import org.drools.compiler.*;\n" +
                "rule R1 when\n" +
                "   Person( name == \"mark\", $country : address#LongAddress.country == \"uk\" )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        Person mark2 = new Person("mark");
        ksession.insert(mark2);

        Person mark3 = new Person("mark");
        mark3.setAddress(new Address());
        ksession.insert(mark3);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineCastOnlyBinding() throws Exception {
        String str = "import org.drools.compiler.*;\n" +
                "rule R1 when\n" +
                "   Person( name == \"mark\", $country : address#LongAddress.country )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        Person mark2 = new Person("mark");
        ksession.insert(mark2);

        Person mark3 = new Person("mark");
        mark3.setAddress(new Address());
        ksession.insert(mark3);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineCastWithFQN() throws Exception {
        String str = "import org.drools.compiler.Person;\n" +
                "rule R1 when\n" +
                "   Person( name == \"mark\", address#org.drools.compiler.LongAddress.country == \"uk\" )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineCastOnRightOperand() throws Exception {
        String str = "import org.drools.compiler.*;\n" +
                "rule R1 when\n" +
                "   $person : Person( )\n" +
                "   String( this == $person.address#LongAddress.country )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);
        ksession.insert("uk");

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineCastOnRightOperandWithFQN() throws Exception {
        String str = "import org.drools.compiler.Person;\n" +
                "rule R1 when\n" +
                "   $person : Person( )\n" +
                "   String( this == $person.address#org.drools.compiler.LongAddress.country )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);
        ksession.insert("uk");

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInferredCast() throws Exception {
        String str = "import org.drools.compiler.*;\n" +
                "rule R1 when\n" +
                "   Person( name == \"mark\", address instanceof LongAddress, address.country == \"uk\" )\n" +
                "then\n" +
                "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        Person mark2 = new Person("mark");
        ksession.insert(mark2);

        Person mark3 = new Person("mark");
        mark3.setAddress(new Address());
        ksession.insert(mark3);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineTypeCast() throws Exception {
        // DROOLS-136
        String str = "import org.drools.compiler.*;\n" +
                     "rule R1 when\n" +
                     " Person( name == \"mark\", address#LongAddress )\n" +
                     "then\n" +
                     "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        Person mark2 = new Person("mark");
        ksession.insert(mark2);

        Person mark3 = new Person("mark");
        mark3.setAddress(new Address());
        ksession.insert(mark3);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }

    @Test
    public void testInlineCastWithNestedAccces() throws Exception {
        // DROOLS-127
        String str = "import org.drools.compiler.*;\n" +
                     "rule R1 when\n" +
                     "   Person( name == \"mark\", address#LongAddress.country.length == 2 )\n" +
                     "then\n" +
                     "end\n";

        KnowledgeBase kbase = loadKnowledgeBaseFromString(str);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        Person mark1 = new Person("mark");
        mark1.setAddress(new LongAddress("uk"));
        ksession.insert(mark1);

        Person mark2 = new Person("mark");
        ksession.insert(mark2);

        Person mark3 = new Person("mark");
        mark3.setAddress(new Address());
        ksession.insert(mark3);

        assertEquals(1, ksession.fireAllRules());
        ksession.dispose();
    }
}
