package tech.trvihnls.testsuites;


import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "tech.trvihnls"
})
@IncludeTags({
        "tag-name"
})
public class SomeTestSuite {
}
