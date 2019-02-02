package ru.gecec.learnphrasebot.model.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SqlResolver {
    private List<String> files;
    private Properties properties;
    private Map<String, String> sqls = new HashMap();

    public SqlResolver() {
    }

    public void init() {
        Iterator var1 = this.files.iterator();

        while(var1.hasNext()) {
            String filename = (String)var1.next();

            try {
                Object stream;
                if (filename.startsWith("classpath:")) {
                    stream = this.getClass().getClassLoader().getResourceAsStream(filename.substring("classpath:".length()));
                } else {
                    stream = new FileInputStream(filename);
                }

                this.parseXml((InputStream)stream);
            } catch (Exception var4) {
                throw new IllegalStateException("Couldn't load sqls from " + filename, var4);
            }
        }

    }

    private void parseXml(InputStream stream) throws IOException, SAXException, ParserConfigurationException {
        Document document = XMLUtil.load(stream);
        NodeList list = document.getDocumentElement().getChildNodes();

        for(int i = 0; i < list.getLength() - 1; ++i) {
            Node sqlNode = list.item(i);
            if ("sql".equals(sqlNode.getNodeName())) {
                String id = ((Element)sqlNode).getAttribute("name");
                String value = XMLUtil.getTextValue((Element)sqlNode);
                if (!StringUtils.isEmpty(value)) {
                    this.sqls.put(id, this.processSql(value));
                }
            }
        }
    }

    private String processSql(String sql) {
        String result = sql;
        String key;
        String value;
        if (this.properties != null) {
            for(Enumeration kyes = this.properties.propertyNames(); kyes.hasMoreElements(); result = result.replace("${" + key + "}", value)) {
                key = (String)kyes.nextElement();
                value = this.properties.getProperty(key);
            }
        }

        return result;
    }

    public String getSql(String queryName) {
        if (!this.sqls.containsKey(queryName)) {
            throw new IllegalArgumentException(queryName + " not found");
        } else {
            return (String)this.sqls.get(queryName);
        }
    }

    public Set<String> getSqlNames() {
        return Collections.unmodifiableSet(this.sqls.keySet());
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}


/*
<sqls>

    <sql name="users.get.user">
        select * from users_json where user_id = :userId
    </sql>

    <sql name="users.update.user">
        update users_json
        set data_json = to_json(:dataJson::json),
        updated = :updated
        where user_id = :userId
    </sql>

    <sql name="users.update.user.has.banners">
        update users_json
        set has_banners = :hasBanners
        where user_id = :userId
    </sql>

    <sql name="users.insert.user">
        insert into users_json(id, user_id, user_id_hash, data_json, updated)
        values(nextval('user_data_seq'), :userId, :userIdHash, to_json(:dataJson::json), :updated)
    </sql>

    <sql name="users.fetch.users">
        select * from users_json
    </sql>

    <sql name="users.fetch.users.by.hash">
        select * from users_json where user_id_hash in (:hashIds)
    </sql>

    <sql name="users.get.user.bounds">
        select min(id) as min_id, max(id) as max_id, false as is_org from users_json
    </sql>

    <sql name="users.remove.user">
        delete from users_json where user_id = :userId
    </sql>

    <sql name="orgs.get.user">
        select * from orgs_json where org_id = :userId
    </sql>

    <sql name="orgs.update.user">
        update orgs_json
        set data_json = to_json(:dataJson::json),
        org_user_id = :orgUserId,
        updated = :updated
        where org_id = :userId
    </sql>

    <sql name="orgs.update.user.has.banners">
        update orgs_json
        set has_banners = :hasBanners
        where org_id = :userId
    </sql>

    <sql name="orgs.insert.user">
        insert into orgs_json(id, org_id, org_user_id, data_json, updated)
        values(nextval('org_data_seq'), :userId, :orgUserId, to_json(:dataJson::json), :updated)
    </sql>

    <sql name="orgs.fetch.users">
        select * from orgs_json
    </sql>

    <sql name="orgs.fetch.users.by.hash">
        select * from orgs_json where org_id_hash in (:hashIds)
    </sql>

    <sql name="orgs.get.user.bounds">
        select min(id) as min_id, max(id) as max_id, true as is_org from orgs_json
    </sql>

    <sql name="orgs.remove.user">
        delete from orgs_json where org_id = :userId
    </sql>

    <sql name="fetch.active.rules">
        select * from rules where is_active
        and is_org = :isOrg
        and is_active_today and type != 'FAKE'
    </sql>

    <sql name="fetch.rules.by.ids">
        select * from rules where is_active and is_active_today and id in (:ruleIds)
    </sql>

    <sql name="fetch.rules.by.ids.today">
        select * from rules where is_active_today and id in (:ruleIds)
    </sql>

    <sql name="fetch.rules.by.ids.only.active">
        select * from rules where is_active and id in (:ruleIds)
    </sql>

    <sql name="fetch.rule.by.id">
        select * from rules where id = :ruleId
    </sql>

    <sql name="log.rule.match">
        insert into rule_match_log (user_id, rule_id, date, notif_sent)
        values(:userId, :ruleId, current_timestamp, :notifSent)
    </sql>

    <sql name="clear.log">
        delete from rule_match_log where date &lt;= (current_date - interval $interval)
    </sql>

    <sql name="get.last.notif.rule.matches">
        select * from user_rule_notif where user_id = :userId and rule_id in (:ruleIds)
    </sql>

    <sql name="add.notif.rules">
        insert into user_rule_notif (user_id, rule_id, date)
        values (:userId, :ruleId, current_timestamp)
        on conflict(user_id, rule_id)
        do update set date = current_timestamp
    </sql>

    <sql name="increment.notif.rule.count">
        insert into rule_counter (rule_id, count) values (:ruleId, :increment) on conflict (rule_id) do update set count = rule_counter.count + :increment
    </sql>

    <sql name="increment.notif.rule.count.today">
        insert into rule_counter (rule_id, count_today) values (:ruleId, :increment) on conflict (rule_id) do update set count_today = rule_counter.count_today + :increment
    </sql>

    <sql name="current.notif.rule.count">
        select count from rule_counter where rule_id = :ruleId
    </sql>

    <sql name="current.notif.rule.count.today">
        select count_today from rule_counter where rule_id = :ruleId
    </sql>

    <sql name="deactivate.rule">
        update rules set is_active = false where id = :ruleId
    </sql>

    <sql name="set.rule.active.today">
        update rules set is_active_today = :isActive where id = :ruleId
    </sql>

    <sql name="activate.rules.today">
        update rules set is_active_today = true where max_notif_count_per_day is not null and max_notif_count_per_day &lt;&gt; 0
    </sql>

    <sql name="set.rules.counters.today">
        update rule_counter set count_today = 0
    </sql>

    <sql name="add.user.banner">
        insert into user_banners (user_id, banner_mnemonic, is_org) values (:userId, :bannerMnemonic, :isOrg)
    </sql>

    <sql name="remove.user.banners.by.user.id">
        delete from user_banners where user_id = :userId and is_org = :isOrg
    </sql>

    <sql name="get.template.by.id">
        select template from banners where id = :id
    </sql>

    <sql name="close.banner">
        insert into closed_banners (user_id, is_org, banner_mnemonic) values (:user_id, :isOrg, :mnemonic)
    </sql>

    <sql name="get.conditions">
        select bc.*, b.status, b.visible, b.e_visible, b.deleted from banner b
        join banner_in_regions br on br.banner_mnemonic = b.mnemonic
        join banner_conditions bc on bc.banner_mnemonic = b.mnemonic
        join banner_places     bp on bp.banner_mnemonic = b.mnemonic
        join banner_places_description bpd on bpd.place_name = bp.place_name
        where bp.place_name in (:places) and (br.region = :region or br.region = '00000000000') and (b.deleted = 'N' or b.status &lt;&gt; 'P') and b.is_targeted = 'N' ${content_not_null_condition}
        and b.mnemonic not in (:closed_banners_mnemonics)
    </sql>


    <sql name="get.closed.banners.by.user.id">
        select banner_mnemonic from closed_banners
        where user_id = :userId and is_org = :isOrg
        order by banner_mnemonic
    </sql>

    <sql name="get.banners.by.mnemonic"><![CDATA[
        select b.mnemonic,
        ${content_bg_image_by_platform},
        b.order_number, b.e_order_number, b.order_number_mp, b.closable, b.e_closable, b.visible, b.e_visible,
        b.deleted, b.status, b.alt_content,  b.slider_title, b.is_template,
        b.is_targeted, bp.place_name, bpd.bg_image as bp_bg_image  from banner b
        join banner_in_regions br on br.banner_mnemonic = b.mnemonic
        join banner_places     bp on bp.banner_mnemonic = b.mnemonic
        join banner_places_description bpd on bpd.place_name = bp.place_name
        where bp.place_name in (:places)
        and (br.region = :region or br.region = '00000000000')
        and (b.deleted = 'N' or b.status <> 'P')
        and b.is_targeted = 'N'
        ${content_not_null_condition}
        and b.mnemonic = :mnemonic
        and (b.email_start_date is null or
        (b.email_start_date <= current_timestamp and b.email_end_date >= current_timestamp))
    ]]></sql>

    <sql name="get.banners.without.conditions"><![CDATA[
        select b.mnemonic,
        ${content_bg_image_by_platform},
        b.order_number, b.e_order_number, b.order_number_mp, b.closable, b.e_closable, b.visible, b.e_visible,
        b.deleted, b.status, b.alt_content,  b.slider_title, b.is_template,
        b.is_targeted, bp.place_name, bpd.bg_image as bp_bg_image from banner b
        join banner_in_regions br on br.banner_mnemonic = b.mnemonic
        join banner_places     bp on bp.banner_mnemonic = b.mnemonic
        join banner_places_description bpd on bpd.place_name = bp.place_name
        where bp.place_name in (:places)
        and (br.region = :region or br.region = '00000000000')
        and (b.deleted = 'N' or b.status <> 'P')
        and b.is_targeted = 'N'
        ${content_not_null_condition}
        and not exists (select 1 from banner_conditions bc where bc.banner_mnemonic = b.mnemonic)
        and b.mnemonic not in (:closed_banners_mnemonics)
        and (b.email_start_date is null or
        (b.email_start_date <= current_timestamp and b.email_end_date >= current_timestamp))
        order by b.order_number, b.e_order_number, b.order_number_mp
    ]]></sql>

    <sql name="get.user.banners"><![CDATA[
        select b.mnemonic,
        ${content_bg_image_by_platform},
        b.order_number, b.e_order_number, b.order_number_mp, b.closable, b.e_closable, b.visible, b.e_visible,
        b.deleted, b.status, b.alt_content,  b.slider_title, b.is_template,
        b.is_targeted, bp.place_name, bpd.bg_image as bp_bg_image  from banner b
        join banner_places bp on bp.banner_mnemonic = b.mnemonic
        join banner_places_description bpd on bpd.place_name = bp.place_name
        and (b.deleted = 'N' or b.status <> 'P')
        and b.is_targeted = 'Y'
        ${content_not_null_condition}
        and bp.place_name in (:places)
        and b.mnemonic in (:banners_mnemonics)
        and (b.email_start_date is null or
        (b.email_start_date <= current_timestamp and b.email_end_date >= current_timestamp))
        order by b.order_number, b.e_order_number, b.order_number_mp
    ]]></sql>

    <sql name="get.banner.place.volume">
        select volume from banner_places_description where place_name = :place_name
    </sql>

    <sql name="get.banner.place.bg">
        select bg_image from banner_places_description where place_name = :place_name
    </sql>

    <sql name="users.load.blacklist">
        select user_id from black_list where not is_org
    </sql>

    <sql name="orgs.load.blacklist">
        select user_id from black_list where is_org
    </sql>

    <sql name="remove.esia_banners.for.user">
        delete from esia_user_banners where user_id = :userId and is_org = :isOrg
        and esia_banner_mnemonic not in (select mnemonic from esia_banner where manual = 'Y')
    </sql>

    <sql name="get.esia_banner.by.mnemonic">
        select mnemonic, type, content, manual, priority, banner_name from esia_banner where mnemonic = :mnemonic and deleted = 'N'
    </sql>

    <sql name="get.esia_banners.for.user"><![CDATA[
        select * from esia_banner eb
        where eb.deleted = 'N'
        and eb.mnemonic in (:bannerMnemonics)
        and eb.mnemonic not in (select ceb.esia_banner_mnemonic from esia_closed_banners ceb where ceb.user_id = :userId)
        and (eb.start_date is null or
        (eb.start_date <= current_timestamp and eb.end_date >= current_timestamp))
    ]]></sql>

    <sql name="close.esia.banner">
        insert into esia_closed_banners (user_id, is_org, esia_banner_mnemonic) values (:userId, :isOrg, :mnemonic)
        on conflict(user_id, is_org, esia_banner_mnemonic) do nothing
    </sql>

    <sql name="close.esia.manual.banner">
        delete from esia_user_banners where user_id = :userId and esia_banner_mnemonic = :mnemonic and is_org = :isOrg
    </sql>

    <sql name="add.esia.banner">
        insert into esia_user_banners (user_id, esia_banner_mnemonic, is_org) values (:userId, :mnemonic, :isOrg)
    </sql>

    <sql name="referer.by.host">
        select * from referers where referer_host = :host
    </sql>

    <sql name="get.user.banner.mnemonics">
        select * from user_banners
        where user_id = :userId and is_org = :isOrg
    </sql>

    <sql name="get.esia.banner.mnemonics">
        select * from esia_user_banners
        where user_id = :userId and is_org = :isOrg
    </sql>

    <sql name="get.task.config.by.type">
        select * from task_config where type = :taskType
    </sql>

    <sql name="get.user.email.banners"><![CDATA[
        select b.* from banner b
        join banner_places bp on bp.banner_mnemonic=b.mnemonic
        where bp.place_name = :bannerPlace
            and b.mnemonic in (:userBanners)
            and (b.deleted = 'N' or b.status <> 'P')
            and b.content_email is not null
            and (b.email_start_date is null or
                (b.email_start_date <= current_timestamp and b.email_end_date >= current_timestamp))
        order by b.order_number
    ]]></sql>

</sqls>*/