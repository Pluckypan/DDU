// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: User.proto

#ifndef PROTOBUF_User_2eproto__INCLUDED
#define PROTOBUF_User_2eproto__INCLUDED

#include <string>

#include <google/protobuf/stubs/common.h>

#if GOOGLE_PROTOBUF_VERSION < 3001000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please update
#error your headers.
#endif
#if 3001000 < GOOGLE_PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/arena.h>
#include <google/protobuf/arenastring.h>
#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/metadata.h>
#include <google/protobuf/message.h>
#include <google/protobuf/repeated_field.h>
#include <google/protobuf/extension_set.h>
#include <google/protobuf/generated_enum_reflection.h>
#include <google/protobuf/unknown_field_set.h>
// @@protoc_insertion_point(includes)

namespace engineer {
namespace echo {

// Internal implementation detail -- do not call these.
void protobuf_AddDesc_User_2eproto();
void protobuf_InitDefaults_User_2eproto();
void protobuf_AssignDesc_User_2eproto();
void protobuf_ShutdownFile_User_2eproto();

class User;
class User_PhoneNumber;

enum User_PhoneType {
  User_PhoneType_MOBILE = 0,
  User_PhoneType_HOME = 1,
  User_PhoneType_WORK = 2
};
bool User_PhoneType_IsValid(int value);
const User_PhoneType User_PhoneType_PhoneType_MIN = User_PhoneType_MOBILE;
const User_PhoneType User_PhoneType_PhoneType_MAX = User_PhoneType_WORK;
const int User_PhoneType_PhoneType_ARRAYSIZE = User_PhoneType_PhoneType_MAX + 1;

const ::google::protobuf::EnumDescriptor* User_PhoneType_descriptor();
inline const ::std::string& User_PhoneType_Name(User_PhoneType value) {
  return ::google::protobuf::internal::NameOfEnum(
    User_PhoneType_descriptor(), value);
}
inline bool User_PhoneType_Parse(
    const ::std::string& name, User_PhoneType* value) {
  return ::google::protobuf::internal::ParseNamedEnum<User_PhoneType>(
    User_PhoneType_descriptor(), name, value);
}
// ===================================================================

class User_PhoneNumber : public ::google::protobuf::Message /* @@protoc_insertion_point(class_definition:engineer.echo.User.PhoneNumber) */ {
 public:
  User_PhoneNumber();
  virtual ~User_PhoneNumber();

  User_PhoneNumber(const User_PhoneNumber& from);

  inline User_PhoneNumber& operator=(const User_PhoneNumber& from) {
    CopyFrom(from);
    return *this;
  }

  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _internal_metadata_.unknown_fields();
  }

  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return _internal_metadata_.mutable_unknown_fields();
  }

  static const ::google::protobuf::Descriptor* descriptor();
  static const User_PhoneNumber& default_instance();

  static const User_PhoneNumber* internal_default_instance();

  void Swap(User_PhoneNumber* other);

  // implements Message ----------------------------------------------

  inline User_PhoneNumber* New() const { return New(NULL); }

  User_PhoneNumber* New(::google::protobuf::Arena* arena) const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const User_PhoneNumber& from);
  void MergeFrom(const User_PhoneNumber& from);
  void Clear();
  bool IsInitialized() const;

  size_t ByteSizeLong() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* InternalSerializeWithCachedSizesToArray(
      bool deterministic, ::google::protobuf::uint8* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const {
    return InternalSerializeWithCachedSizesToArray(false, output);
  }
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const;
  void InternalSwap(User_PhoneNumber* other);
  void UnsafeMergeFrom(const User_PhoneNumber& from);
  private:
  inline ::google::protobuf::Arena* GetArenaNoVirtual() const {
    return _internal_metadata_.arena();
  }
  inline void* MaybeArenaPtr() const {
    return _internal_metadata_.raw_arena_ptr();
  }
  public:

  ::google::protobuf::Metadata GetMetadata() const;

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  // required string number = 1;
  bool has_number() const;
  void clear_number();
  static const int kNumberFieldNumber = 1;
  const ::std::string& number() const;
  void set_number(const ::std::string& value);
  void set_number(const char* value);
  void set_number(const char* value, size_t size);
  ::std::string* mutable_number();
  ::std::string* release_number();
  void set_allocated_number(::std::string* number);

  // optional .engineer.echo.User.PhoneType type = 2 [default = HOME];
  bool has_type() const;
  void clear_type();
  static const int kTypeFieldNumber = 2;
  ::engineer::echo::User_PhoneType type() const;
  void set_type(::engineer::echo::User_PhoneType value);

  // @@protoc_insertion_point(class_scope:engineer.echo.User.PhoneNumber)
 private:
  inline void set_has_number();
  inline void clear_has_number();
  inline void set_has_type();
  inline void clear_has_type();

  ::google::protobuf::internal::InternalMetadataWithArena _internal_metadata_;
  ::google::protobuf::internal::HasBits<1> _has_bits_;
  mutable int _cached_size_;
  ::google::protobuf::internal::ArenaStringPtr number_;
  int type_;
  friend void  protobuf_InitDefaults_User_2eproto_impl();
  friend void  protobuf_AddDesc_User_2eproto_impl();
  friend void protobuf_AssignDesc_User_2eproto();
  friend void protobuf_ShutdownFile_User_2eproto();

  void InitAsDefaultInstance();
};
extern ::google::protobuf::internal::ExplicitlyConstructed<User_PhoneNumber> User_PhoneNumber_default_instance_;

// -------------------------------------------------------------------

class User : public ::google::protobuf::Message /* @@protoc_insertion_point(class_definition:engineer.echo.User) */ {
 public:
  User();
  virtual ~User();

  User(const User& from);

  inline User& operator=(const User& from) {
    CopyFrom(from);
    return *this;
  }

  inline const ::google::protobuf::UnknownFieldSet& unknown_fields() const {
    return _internal_metadata_.unknown_fields();
  }

  inline ::google::protobuf::UnknownFieldSet* mutable_unknown_fields() {
    return _internal_metadata_.mutable_unknown_fields();
  }

  static const ::google::protobuf::Descriptor* descriptor();
  static const User& default_instance();

  static const User* internal_default_instance();

  void Swap(User* other);

  // implements Message ----------------------------------------------

  inline User* New() const { return New(NULL); }

  User* New(::google::protobuf::Arena* arena) const;
  void CopyFrom(const ::google::protobuf::Message& from);
  void MergeFrom(const ::google::protobuf::Message& from);
  void CopyFrom(const User& from);
  void MergeFrom(const User& from);
  void Clear();
  bool IsInitialized() const;

  size_t ByteSizeLong() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  ::google::protobuf::uint8* InternalSerializeWithCachedSizesToArray(
      bool deterministic, ::google::protobuf::uint8* output) const;
  ::google::protobuf::uint8* SerializeWithCachedSizesToArray(::google::protobuf::uint8* output) const {
    return InternalSerializeWithCachedSizesToArray(false, output);
  }
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const;
  void InternalSwap(User* other);
  void UnsafeMergeFrom(const User& from);
  private:
  inline ::google::protobuf::Arena* GetArenaNoVirtual() const {
    return _internal_metadata_.arena();
  }
  inline void* MaybeArenaPtr() const {
    return _internal_metadata_.raw_arena_ptr();
  }
  public:

  ::google::protobuf::Metadata GetMetadata() const;

  // nested types ----------------------------------------------------

  typedef User_PhoneNumber PhoneNumber;

  typedef User_PhoneType PhoneType;
  static const PhoneType MOBILE =
    User_PhoneType_MOBILE;
  static const PhoneType HOME =
    User_PhoneType_HOME;
  static const PhoneType WORK =
    User_PhoneType_WORK;
  static inline bool PhoneType_IsValid(int value) {
    return User_PhoneType_IsValid(value);
  }
  static const PhoneType PhoneType_MIN =
    User_PhoneType_PhoneType_MIN;
  static const PhoneType PhoneType_MAX =
    User_PhoneType_PhoneType_MAX;
  static const int PhoneType_ARRAYSIZE =
    User_PhoneType_PhoneType_ARRAYSIZE;
  static inline const ::google::protobuf::EnumDescriptor*
  PhoneType_descriptor() {
    return User_PhoneType_descriptor();
  }
  static inline const ::std::string& PhoneType_Name(PhoneType value) {
    return User_PhoneType_Name(value);
  }
  static inline bool PhoneType_Parse(const ::std::string& name,
      PhoneType* value) {
    return User_PhoneType_Parse(name, value);
  }

  // accessors -------------------------------------------------------

  // required string name = 1;
  bool has_name() const;
  void clear_name();
  static const int kNameFieldNumber = 1;
  const ::std::string& name() const;
  void set_name(const ::std::string& value);
  void set_name(const char* value);
  void set_name(const char* value, size_t size);
  ::std::string* mutable_name();
  ::std::string* release_name();
  void set_allocated_name(::std::string* name);

  // required int32 id = 2;
  bool has_id() const;
  void clear_id();
  static const int kIdFieldNumber = 2;
  ::google::protobuf::int32 id() const;
  void set_id(::google::protobuf::int32 value);

  // optional string email = 3;
  bool has_email() const;
  void clear_email();
  static const int kEmailFieldNumber = 3;
  const ::std::string& email() const;
  void set_email(const ::std::string& value);
  void set_email(const char* value);
  void set_email(const char* value, size_t size);
  ::std::string* mutable_email();
  ::std::string* release_email();
  void set_allocated_email(::std::string* email);

  // repeated .engineer.echo.User.PhoneNumber phone = 4;
  int phone_size() const;
  void clear_phone();
  static const int kPhoneFieldNumber = 4;
  const ::engineer::echo::User_PhoneNumber& phone(int index) const;
  ::engineer::echo::User_PhoneNumber* mutable_phone(int index);
  ::engineer::echo::User_PhoneNumber* add_phone();
  ::google::protobuf::RepeatedPtrField< ::engineer::echo::User_PhoneNumber >*
      mutable_phone();
  const ::google::protobuf::RepeatedPtrField< ::engineer::echo::User_PhoneNumber >&
      phone() const;

  // optional string extra = 5;
  bool has_extra() const;
  void clear_extra();
  static const int kExtraFieldNumber = 5;
  const ::std::string& extra() const;
  void set_extra(const ::std::string& value);
  void set_extra(const char* value);
  void set_extra(const char* value, size_t size);
  ::std::string* mutable_extra();
  ::std::string* release_extra();
  void set_allocated_extra(::std::string* extra);

  // @@protoc_insertion_point(class_scope:engineer.echo.User)
 private:
  inline void set_has_name();
  inline void clear_has_name();
  inline void set_has_id();
  inline void clear_has_id();
  inline void set_has_email();
  inline void clear_has_email();
  inline void set_has_extra();
  inline void clear_has_extra();

  // helper for ByteSizeLong()
  size_t RequiredFieldsByteSizeFallback() const;

  ::google::protobuf::internal::InternalMetadataWithArena _internal_metadata_;
  ::google::protobuf::internal::HasBits<1> _has_bits_;
  mutable int _cached_size_;
  ::google::protobuf::RepeatedPtrField< ::engineer::echo::User_PhoneNumber > phone_;
  ::google::protobuf::internal::ArenaStringPtr name_;
  ::google::protobuf::internal::ArenaStringPtr email_;
  ::google::protobuf::internal::ArenaStringPtr extra_;
  ::google::protobuf::int32 id_;
  friend void  protobuf_InitDefaults_User_2eproto_impl();
  friend void  protobuf_AddDesc_User_2eproto_impl();
  friend void protobuf_AssignDesc_User_2eproto();
  friend void protobuf_ShutdownFile_User_2eproto();

  void InitAsDefaultInstance();
};
extern ::google::protobuf::internal::ExplicitlyConstructed<User> User_default_instance_;

// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// User_PhoneNumber

// required string number = 1;
inline bool User_PhoneNumber::has_number() const {
  return (_has_bits_[0] & 0x00000001u) != 0;
}
inline void User_PhoneNumber::set_has_number() {
  _has_bits_[0] |= 0x00000001u;
}
inline void User_PhoneNumber::clear_has_number() {
  _has_bits_[0] &= ~0x00000001u;
}
inline void User_PhoneNumber::clear_number() {
  number_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  clear_has_number();
}
inline const ::std::string& User_PhoneNumber::number() const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.PhoneNumber.number)
  return number_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User_PhoneNumber::set_number(const ::std::string& value) {
  set_has_number();
  number_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:engineer.echo.User.PhoneNumber.number)
}
inline void User_PhoneNumber::set_number(const char* value) {
  set_has_number();
  number_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:engineer.echo.User.PhoneNumber.number)
}
inline void User_PhoneNumber::set_number(const char* value, size_t size) {
  set_has_number();
  number_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:engineer.echo.User.PhoneNumber.number)
}
inline ::std::string* User_PhoneNumber::mutable_number() {
  set_has_number();
  // @@protoc_insertion_point(field_mutable:engineer.echo.User.PhoneNumber.number)
  return number_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline ::std::string* User_PhoneNumber::release_number() {
  // @@protoc_insertion_point(field_release:engineer.echo.User.PhoneNumber.number)
  clear_has_number();
  return number_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User_PhoneNumber::set_allocated_number(::std::string* number) {
  if (number != NULL) {
    set_has_number();
  } else {
    clear_has_number();
  }
  number_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), number);
  // @@protoc_insertion_point(field_set_allocated:engineer.echo.User.PhoneNumber.number)
}

// optional .engineer.echo.User.PhoneType type = 2 [default = HOME];
inline bool User_PhoneNumber::has_type() const {
  return (_has_bits_[0] & 0x00000002u) != 0;
}
inline void User_PhoneNumber::set_has_type() {
  _has_bits_[0] |= 0x00000002u;
}
inline void User_PhoneNumber::clear_has_type() {
  _has_bits_[0] &= ~0x00000002u;
}
inline void User_PhoneNumber::clear_type() {
  type_ = 1;
  clear_has_type();
}
inline ::engineer::echo::User_PhoneType User_PhoneNumber::type() const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.PhoneNumber.type)
  return static_cast< ::engineer::echo::User_PhoneType >(type_);
}
inline void User_PhoneNumber::set_type(::engineer::echo::User_PhoneType value) {
  assert(::engineer::echo::User_PhoneType_IsValid(value));
  set_has_type();
  type_ = value;
  // @@protoc_insertion_point(field_set:engineer.echo.User.PhoneNumber.type)
}

inline const User_PhoneNumber* User_PhoneNumber::internal_default_instance() {
  return &User_PhoneNumber_default_instance_.get();
}
// -------------------------------------------------------------------

// User

// required string name = 1;
inline bool User::has_name() const {
  return (_has_bits_[0] & 0x00000001u) != 0;
}
inline void User::set_has_name() {
  _has_bits_[0] |= 0x00000001u;
}
inline void User::clear_has_name() {
  _has_bits_[0] &= ~0x00000001u;
}
inline void User::clear_name() {
  name_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  clear_has_name();
}
inline const ::std::string& User::name() const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.name)
  return name_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User::set_name(const ::std::string& value) {
  set_has_name();
  name_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:engineer.echo.User.name)
}
inline void User::set_name(const char* value) {
  set_has_name();
  name_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:engineer.echo.User.name)
}
inline void User::set_name(const char* value, size_t size) {
  set_has_name();
  name_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:engineer.echo.User.name)
}
inline ::std::string* User::mutable_name() {
  set_has_name();
  // @@protoc_insertion_point(field_mutable:engineer.echo.User.name)
  return name_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline ::std::string* User::release_name() {
  // @@protoc_insertion_point(field_release:engineer.echo.User.name)
  clear_has_name();
  return name_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User::set_allocated_name(::std::string* name) {
  if (name != NULL) {
    set_has_name();
  } else {
    clear_has_name();
  }
  name_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), name);
  // @@protoc_insertion_point(field_set_allocated:engineer.echo.User.name)
}

// required int32 id = 2;
inline bool User::has_id() const {
  return (_has_bits_[0] & 0x00000002u) != 0;
}
inline void User::set_has_id() {
  _has_bits_[0] |= 0x00000002u;
}
inline void User::clear_has_id() {
  _has_bits_[0] &= ~0x00000002u;
}
inline void User::clear_id() {
  id_ = 0;
  clear_has_id();
}
inline ::google::protobuf::int32 User::id() const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.id)
  return id_;
}
inline void User::set_id(::google::protobuf::int32 value) {
  set_has_id();
  id_ = value;
  // @@protoc_insertion_point(field_set:engineer.echo.User.id)
}

// optional string email = 3;
inline bool User::has_email() const {
  return (_has_bits_[0] & 0x00000004u) != 0;
}
inline void User::set_has_email() {
  _has_bits_[0] |= 0x00000004u;
}
inline void User::clear_has_email() {
  _has_bits_[0] &= ~0x00000004u;
}
inline void User::clear_email() {
  email_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  clear_has_email();
}
inline const ::std::string& User::email() const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.email)
  return email_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User::set_email(const ::std::string& value) {
  set_has_email();
  email_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:engineer.echo.User.email)
}
inline void User::set_email(const char* value) {
  set_has_email();
  email_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:engineer.echo.User.email)
}
inline void User::set_email(const char* value, size_t size) {
  set_has_email();
  email_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:engineer.echo.User.email)
}
inline ::std::string* User::mutable_email() {
  set_has_email();
  // @@protoc_insertion_point(field_mutable:engineer.echo.User.email)
  return email_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline ::std::string* User::release_email() {
  // @@protoc_insertion_point(field_release:engineer.echo.User.email)
  clear_has_email();
  return email_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User::set_allocated_email(::std::string* email) {
  if (email != NULL) {
    set_has_email();
  } else {
    clear_has_email();
  }
  email_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), email);
  // @@protoc_insertion_point(field_set_allocated:engineer.echo.User.email)
}

// repeated .engineer.echo.User.PhoneNumber phone = 4;
inline int User::phone_size() const {
  return phone_.size();
}
inline void User::clear_phone() {
  phone_.Clear();
}
inline const ::engineer::echo::User_PhoneNumber& User::phone(int index) const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.phone)
  return phone_.Get(index);
}
inline ::engineer::echo::User_PhoneNumber* User::mutable_phone(int index) {
  // @@protoc_insertion_point(field_mutable:engineer.echo.User.phone)
  return phone_.Mutable(index);
}
inline ::engineer::echo::User_PhoneNumber* User::add_phone() {
  // @@protoc_insertion_point(field_add:engineer.echo.User.phone)
  return phone_.Add();
}
inline ::google::protobuf::RepeatedPtrField< ::engineer::echo::User_PhoneNumber >*
User::mutable_phone() {
  // @@protoc_insertion_point(field_mutable_list:engineer.echo.User.phone)
  return &phone_;
}
inline const ::google::protobuf::RepeatedPtrField< ::engineer::echo::User_PhoneNumber >&
User::phone() const {
  // @@protoc_insertion_point(field_list:engineer.echo.User.phone)
  return phone_;
}

// optional string extra = 5;
inline bool User::has_extra() const {
  return (_has_bits_[0] & 0x00000010u) != 0;
}
inline void User::set_has_extra() {
  _has_bits_[0] |= 0x00000010u;
}
inline void User::clear_has_extra() {
  _has_bits_[0] &= ~0x00000010u;
}
inline void User::clear_extra() {
  extra_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  clear_has_extra();
}
inline const ::std::string& User::extra() const {
  // @@protoc_insertion_point(field_get:engineer.echo.User.extra)
  return extra_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User::set_extra(const ::std::string& value) {
  set_has_extra();
  extra_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:engineer.echo.User.extra)
}
inline void User::set_extra(const char* value) {
  set_has_extra();
  extra_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:engineer.echo.User.extra)
}
inline void User::set_extra(const char* value, size_t size) {
  set_has_extra();
  extra_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:engineer.echo.User.extra)
}
inline ::std::string* User::mutable_extra() {
  set_has_extra();
  // @@protoc_insertion_point(field_mutable:engineer.echo.User.extra)
  return extra_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline ::std::string* User::release_extra() {
  // @@protoc_insertion_point(field_release:engineer.echo.User.extra)
  clear_has_extra();
  return extra_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
inline void User::set_allocated_extra(::std::string* extra) {
  if (extra != NULL) {
    set_has_extra();
  } else {
    clear_has_extra();
  }
  extra_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), extra);
  // @@protoc_insertion_point(field_set_allocated:engineer.echo.User.extra)
}

inline const User* User::internal_default_instance() {
  return &User_default_instance_.get();
}
#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS
// -------------------------------------------------------------------


// @@protoc_insertion_point(namespace_scope)

}  // namespace echo
}  // namespace engineer

#ifndef SWIG
namespace google {
namespace protobuf {

template <> struct is_proto_enum< ::engineer::echo::User_PhoneType> : ::google::protobuf::internal::true_type {};
template <>
inline const EnumDescriptor* GetEnumDescriptor< ::engineer::echo::User_PhoneType>() {
  return ::engineer::echo::User_PhoneType_descriptor();
}

}  // namespace protobuf
}  // namespace google
#endif  // SWIG

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_User_2eproto__INCLUDED
